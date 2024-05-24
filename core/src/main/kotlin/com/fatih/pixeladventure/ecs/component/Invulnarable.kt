package com.fatih.pixeladventure.ecs.component

import com.fatih.pixeladventure.util.FRUIT_BIT
import com.fatih.pixeladventure.util.GROUND_BIT
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import kotlin.experimental.or
import kotlin.experimental.xor

data class Invulnarable(var time : Float,var isFruitEffect : Boolean = false,var fireFruitEventOnce: Boolean = true) : Component <Invulnarable> {

    override fun type() = Invulnarable

    override fun World.onAdd(entity: Entity) {
        fireFruitEventOnce = true
        entity[Physic].body.fixtureList
            .filterNot{ it.userData == "hitbox" }
            .forEach { it.filterData.maskBits = GROUND_BIT or PLATFORM_BIT or FRUIT_BIT}
    }

    override fun World.onRemove(entity: Entity) {
        if (entity hasNo  Physic) return
        entity[Physic].body.fixtureList.filterNot {
                it.userData == "hitbox"
            }.forEach {
                it.filterData.maskBits = if (it.userData == "footFixture") GameObject.PLAYER.maskBits or PLATFORM_BIT else GameObject.PLAYER.maskBits

            }
    }

    companion object : ComponentType<Invulnarable>()

}
