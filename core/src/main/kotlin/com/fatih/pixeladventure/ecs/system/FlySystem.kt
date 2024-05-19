package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Fly
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class FlySystem : IteratingSystem(family = family { all(Fly) }) {

    override fun onTickEntity(entity: Entity) {
        if (entity hasNo Fly) return
        val flyComp = entity[Fly]
        val jumpComp = entity[Jump]
        val (body) = entity[Physic]
        var (flyTimer) = flyComp
        flyTimer -= deltaTime
        flyComp.timer = flyTimer
        if (flyTimer <= 0f) {
            entity.configure {
                it -= Fly
            }
            return
        }
        if (jumpComp.jump){
            body.setLinearVelocity(body.linearVelocity.x, 9f)
        }

    }
}
