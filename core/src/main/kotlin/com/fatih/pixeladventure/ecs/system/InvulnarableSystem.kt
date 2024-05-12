package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class InvulnarableSystem : IteratingSystem(family = World.family { all(Invulnarable) }) {

    override fun onTickEntity(entity: Entity) {
        val invComp = entity[Invulnarable]
        invComp.time -= deltaTime
        if (invComp.time <= 0f){
            entity.configure { it -= Invulnarable }
        }
    }
}
