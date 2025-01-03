package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class Graphic(val sprite : Sprite) : Component <Graphic> , Comparable<Graphic> {

    val center  = vec2()
        get(){
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y + sprite.height/2f
            return field
        }

    operator fun component2() = center

    override fun type() = Graphic

    companion object : ComponentType<Graphic>()

    override fun compareTo(other: Graphic): Int = when{
        sprite.y < other.sprite.y -> -1
        sprite.y > other.sprite.y -> 1
        sprite.x < other.sprite.x -> -1
        sprite.x > other.sprite.x -> 1
        else -> 0
    }

}
