package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType{
    IDLE,FALL,RUN,JUMP,DOUBLE_JUMP,HIT,OFF,ON,AGGRO,NONE,START,WAVE;
    val atlasKey : String = this.name.lowercase()
}

data class Animation(var gdxAnimation : GdxAnimation? = null,var timer : Float = 0f,var frameDuration : Float,var nextAnimation : AnimationType? = null) : Component <Animation> {

    override fun type() = Animation

    companion object : ComponentType<Animation>()

}
