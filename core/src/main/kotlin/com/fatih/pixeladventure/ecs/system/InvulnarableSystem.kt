package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class InvulnarableSystem : IteratingSystem(family = family { all(Invulnarable) }) {

    override fun onTickEntity(entity: Entity) {
        if (entity hasNo  Invulnarable) return
        val invComp = entity[Invulnarable]
        if (invComp.time <= 0f){
            entity.configure { it -= Invulnarable }
        }
        invComp.time -= deltaTime
    }
}
