package com.fatih.pixeladventure.ecs.component

import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import kotlin.experimental.or
import kotlin.experimental.xor

data class Invulnarable(var time : Float,var isFruitEffect : Boolean = false,var fireKiwiEventOnce: Boolean = true) : Component <Invulnarable> {

    override fun type() = Invulnarable

    override fun World.onAdd(entity: Entity) {
        fireKiwiEventOnce = true
        entity[Physic].body.fixtureList
            .filterNot{ it.userData == "hitbox" }
            .forEach { it.filterData.maskBits = it.filterData.maskBits xor ROCK_HEAD_BIT }
    }

    override fun World.onRemove(entity: Entity) {
        if (entity hasNo  Physic) return
        entity[Physic].body.fixtureList.filterNot {
                it.userData == "hitbox"
            }.forEach {
                it.filterData.maskBits = it.filterData.maskBits or ROCK_HEAD_BIT
            }
    }

    companion object : ComponentType<Invulnarable>()

}
