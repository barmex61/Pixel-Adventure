package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class MoveDirection(val value : Int){
    LEFT(-1),RIGHT(1),NONE(0);
    companion object{
        fun of(value : Int): MoveDirection {
            return when(value){
                1 -> RIGHT
                -1 -> LEFT
                else -> NONE
            }
        }
    }
}

data class Move(var flipX : Boolean = false,var direction : MoveDirection = MoveDirection.NONE, var current : Float = 0f, var max : Float , var timer : Float = 0f, var timeToMax : Float) : Component <Move> {

    override fun type() = Move

    companion object : ComponentType<Move>()

}
