package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class Fly(var timer : Float = 2f,var fireFruitEventOnce: Boolean = true) : Component <Fly> {

    override fun World.onAdd(entity: Entity) {
        fireFruitEventOnce = true
    }

    override fun type() = Fly

    companion object : ComponentType<Fly>()

}
