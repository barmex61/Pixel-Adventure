package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Remove
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class RemoveSystem : IteratingSystem(family = World.family { all(Remove) }) {

    override fun onTickEntity(entity: Entity) {
        val removeComp = entity[Remove]
        if (removeComp.removePhysic && entity has Physic){
            removeComp.removePhysic = false
            entity.configure { it -= Physic }
        }
        removeComp.duration -= deltaTime
        if (removeComp.duration <= 0f){
            entity.remove()
        }

    }
}
