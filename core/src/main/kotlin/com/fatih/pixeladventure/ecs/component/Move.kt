package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class MoveDirection(val value : Int){
    LEFT(-1),RIGHT(1),NONE(0),UP(2),DOWN(3);
    fun opposite() : MoveDirection {
        return when(this){
            LEFT -> RIGHT
            RIGHT -> LEFT
            UP -> DOWN
            DOWN -> UP
            NONE -> NONE
        }
    }
    companion object{
        fun of(value : Int): MoveDirection {
            return when(value){
                1 -> RIGHT
                -1 -> LEFT
                2 -> UP
                3 -> DOWN
                else -> NONE
            }
        }
    }
}

data class Move(var flipX : Boolean = false,
                var direction : MoveDirection = MoveDirection.NONE,
                var current : Float = 0f,
                var max : Float ,
                var timer : Float = 0f,
                var timeToMax : Float,
                var previousDirection : MoveDirection = MoveDirection.NONE) : Component <Move> {

    override fun type() = Move

    companion object : ComponentType<Move>()

}
