package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.fatih.pixeladventure.ecs.component.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.GdxAnimation
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Tiled
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.TextureAtlasAsset
import com.github.quillraven.fleks.EachFrame
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import ktx.app.gdxError
import ktx.log.logger
import kotlin.math.abs
import kotlin.math.max

class AnimationSystem(
    assets: Assets = World.inject()
) : IteratingSystem(family = family { all(Animation,Graphic) }) {

    private val objectAtlas = assets[TextureAtlasAsset.GAMEOBJECT]
    private val gdxAnimationCache = mutableMapOf<String,GdxAnimation>()

    override fun onTickEntity(entity: Entity) {
        val animationComp = entity[Animation]
        if (animationComp.gdxAnimation == null) return
        val (gdxAnimation,timer,_,_) = animationComp
        val (sprite) = entity[Graphic]
        sprite.setRegion(gdxAnimation!!.getKeyFrame(timer).apply {
            entity.getOrNull(Move)?.let { moveComp ->
                if (moveComp.flipX != isFlipX){
                    flip(true,false)
                }
            }
        })
        val velocityMultiplier = if (entity has Physic) abs(entity[Physic].body.linearVelocity.x / 4f) else 1f
        animationComp.timer += deltaTime * max(1f,velocityMultiplier)

    }

    fun entityAnimation(entity: Entity, animationType: AnimationType,playMode: PlayMode) {

        val (_,gameObject) = entity[Tiled]
        val animationAtlasKey = "${gameObject.atlasKey}/${animationType.atlasKey}"
        val gdxAnimation = gdxAnimationCache.getOrPut(animationAtlasKey){
            val regions = objectAtlas.findRegions(animationAtlasKey)

            if (regions.isEmpty){
                gdxError("There are no regions for the animation $animationAtlasKey")
            }
            GdxAnimation(entity[Animation].frameDuration,regions,playMode)
        }
        if (gdxAnimationCache.size > 100){
            log.info { "Animation cache is larger than 100" }
        }
        val animationComp = entity[Animation]
        animationComp.timer = 0f
        animationComp.gdxAnimation = gdxAnimation
        animationComp.playMode = playMode
        animationComp.animationType = animationType
        entity[Graphic].sprite.setRegion(gdxAnimation.getKeyFrame(0f).apply {
            entity.getOrNull(Move)?.let { moveComp ->
                if (moveComp.flipX != isFlipX){
                    flip(true,false)
                }
            }
        })
    }

    companion object{
        private val log = logger<AnimationSystem>()
    }

}
