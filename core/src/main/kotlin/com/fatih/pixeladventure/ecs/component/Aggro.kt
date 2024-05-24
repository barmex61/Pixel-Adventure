package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Aggro(var aggroEntities : MutableSet<Entity> = mutableSetOf(), val sourceLocation : Vector2, var targetEntity : Entity = Entity.NONE, var verticalCollision : Boolean = false, var horizontalCollision : Boolean = false) : Component <Aggro> {

    override fun type() = Aggro

    companion object : ComponentType<Aggro>()

}
