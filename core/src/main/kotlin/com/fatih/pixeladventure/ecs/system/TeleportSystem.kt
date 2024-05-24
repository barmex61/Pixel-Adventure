package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.fatih.pixeladventure.ai.PlayerState
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Fly
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Teleport
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.PlayerOutOfMapEvent
import com.fatih.pixeladventure.event.RestartLevelEvent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class TeleportSystem : IteratingSystem(family = family { all(Teleport) }) , GameEventListener{

    override fun onTickEntity(entity: Entity) {
        val teleportComp = entity[Teleport]
        val (spawnLocation,doTeleport) = teleportComp
        if (!doTeleport) return
        entity[Move].max = entity[Move].defaultMax
        entity.getOrNull(Fly)?.timer = 0f
        val physicComp = entity[Physic]
        entity.configure {
            if (it hasNo Invulnarable && it hasNo Blink){
                it += Invulnarable(1f)
                it += Blink(0.75f,0.075f)
            }
        }
        physicComp.body.setTransform(spawnLocation,0f)
        teleportComp.doTeleport = false
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is PlayerOutOfMapEvent ->{
                val playerEntity = gameEvent.playerEntity
                playerEntity[Teleport].doTeleport = true
                val lifeComp = playerEntity.getOrNull(Life)?:return
                lifeComp.current = (lifeComp.current -1).coerceAtLeast(0)
                GameEventDispatcher.fireEvent(EntityLifeChangeEvent(playerEntity.getOrNull(Life)?.current?:0))
                playerEntity[State].stateMachine.changeState(PlayerState.HIT)
                if (playerEntity hasNo  Blink){
                    playerEntity.configure {
                        it += Flash(color = Color.RED, weight = 0.75f, amount = 1, delay = 0.15f)
                    }
                }
            }
            else -> Unit
        }
    }
}

