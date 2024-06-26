package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType{
    APPEARING,IDLE,FALL,RUN,JUMP,DOUBLE_JUMP,HIT,OFF,ON,AGGRO,NONE,START,WAVE,HIT_LEFT,HIT_TOP,HIT_RIGHT,HIT_BOTTOM;
    val atlasKey : String = this.name.lowercase()
}

data class Animation(var gdxAnimation : GdxAnimation? = null,var timer : Float = 0f,var frameDuration : Float,var playMode: PlayMode = PlayMode.LOOP,var animationType: AnimationType = AnimationType.IDLE) : Component <Animation> {

    override fun type() = Animation

    companion object : ComponentType<Animation>()

}
