package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType{
    IDLE,FALL,RUN,JUMP,DOUBLE_JUMP,HIT;
    val atlasKey : String = this.name.lowercase()
}

data class Animation(var gdxAnimation : GdxAnimation? = null,var timer : Float = 0f) : Component <Animation> {

    override fun type() = Animation

    companion object : ComponentType<Animation>() {
        const val DEFAULT_FRAME_DURATION = 1/15f
    }

}