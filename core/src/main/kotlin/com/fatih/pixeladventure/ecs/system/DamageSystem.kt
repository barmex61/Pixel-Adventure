package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.fatih.pixeladventure.ai.PlayerState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

class DamageSystem : IteratingSystem(family = family{all(Life,DamageTaken).none(Invulnarable,Blink)}) {

    override fun onTickEntity(entity: Entity) {
        val damageTaken = entity[DamageTaken]
        val lifeComp = entity[Life]
        val (damageAmount) = damageTaken
        if (entity has Invulnarable) return
        lifeComp.current = (lifeComp.current - damageAmount).coerceAtLeast(0)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(lifeComp.current))
        if (entity has EntityTag.PLAYER){
            entity.configure {
                it += Invulnarable(1.5f)
                it += Blink(1.3f,0.075f)
                it += Flash(color = Color.RED, weight = 0.75f, amount = 1, delay = 0.15f)
            }
            entity[State].stateMachine.changeState(PlayerState.HIT)
        }
    }
}
