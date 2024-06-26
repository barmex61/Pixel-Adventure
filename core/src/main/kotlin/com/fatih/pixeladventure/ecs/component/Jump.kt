package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Jump(
    var maxHeight : Float,
    val lowerFeet : Vector2,
    val upperFeet : Vector2,
    var jump : Boolean = false,
    var doubleJump : Boolean = false,
    var wallJumpFruitTimer : Float = 0f,
    var doubleJumpFruitTimer : Float = 0f,
    var doubleJumpCounter : Int = 0
    ) : Component <Jump> {

    override fun type() = Jump

    companion object : ComponentType<Jump>()

}
