package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class Physic(val body : Body,val previousPosition : Vector2 = vec2()) : Component <Physic> {

    override fun type() = Physic

    companion object : ComponentType<Physic>()

}