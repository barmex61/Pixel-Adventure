package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem.Companion.DEBUG_RECT
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.GROUND_BIT
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.box2d.query
import kotlin.math.sqrt

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
    private val audioService: AudioService = World.inject()
): IteratingSystem(family = World.family { all(Jump) }) {

    override fun onTickEntity(entity: Entity) {
        val jumpComps = entity[Jump]
        val (body,_) = entity[Physic]
        val (maxHeight , lowerFeet,upperFeet,buffer) = entity[Jump]
        if (buffer == 0f ){
            return
        }
        jumpComps.buffer = (jumpComps.buffer -deltaTime).coerceAtLeast(0f)
        if (!MathUtils.isEqual(body.linearVelocity.y,0f,2f)){
            return
        }

        val lowerX = body.position.x + lowerFeet.x - 0.15f
        val lowerY = body.position.y + lowerFeet.y - 0.15f
        val upperX = body.position.x + upperFeet.x + 0.15f
        val upperY = body.position.y + upperFeet.y + 0.15f

        DEBUG_RECT.set(lowerX,lowerY,upperX-lowerX,upperY-lowerY)

        physicWorld.query(lowerX,lowerY,upperX,upperY){fixture ->
            if (fixture.filterData.categoryBits == GROUND_BIT ||
                fixture.filterData.categoryBits == ROCK_HEAD_BIT ||
                fixture.filterData.categoryBits == PLATFORM_BIT){
                jumpComps.buffer = 0f
                val gravityY = if (physicWorld.gravity.y == 0f) 1f else physicWorld.gravity.y
                body.setLinearVelocity(body.linearVelocity.x, sqrt(2 * maxHeight * -gravityY))
                audioService.play(SoundAsset.JUMP)
            }

            return@query true
        }


    }
}
