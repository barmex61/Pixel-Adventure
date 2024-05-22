package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.event.EndFruitEffectEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.util.FruitDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class InvulnarableSystem : IteratingSystem(family = family { all(Invulnarable) }) {

    override fun onTickEntity(entity: Entity) {
        entity.getOrNull(Invulnarable)?.let { invComp->
            var (time,isFruitEffect,fireKiwiEventOnce) = invComp
            time -= deltaTime
            if (time <= 0.75f && fireKiwiEventOnce && isFruitEffect){
                fireKiwiEventOnce = false
                println("fire")
                GameEventDispatcher.fireEvent(EndFruitEffectEvent(FruitDrawable.KIWI,0))
            }
            if (time <= 0f){
                entity.configure { it -= Invulnarable }
            }
            invComp.time = time
            invComp.fireFruitEventOnce = fireKiwiEventOnce
        }
    }
}
