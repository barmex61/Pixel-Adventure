package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Fly
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.event.EndFruitEffectEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.util.FruitDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class FlySystem : IteratingSystem(family = family { all(Fly) }) {

    override fun onTickEntity(entity: Entity) {
        val flyComp = entity[Fly]
        val jumpComp = entity[Jump]
        val (body) = entity[Physic]
        var (flyTimer,fireFruitEventOnce) = flyComp
        flyTimer -= deltaTime
        flyComp.timer = flyTimer
        if (flyTimer <= 0.75f && fireFruitEventOnce){
            GameEventDispatcher.fireEvent(EndFruitEffectEvent(FruitDrawable.PINEAPPLE,0))
            println("fire")
            flyComp.fireFruitEventOnce = false
        }
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
