package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.fatih.pixeladventure.ai.GameObjectState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Teleport
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.PlayerDeathEvent
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.inject

class DamageSystem(
    private val audioService: AudioService = inject()
) : IteratingSystem(family = World.family { all(DamageTaken,Life).none(Invulnarable) }) , GameEventListener{

    override fun onTickEntity(entity: Entity) {
        val (damageAmount) = entity[DamageTaken]
        val lifeComp = entity[Life]
        lifeComp.current = (lifeComp.current - damageAmount).coerceAtLeast(0)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(entity))
        if (entity has EntityTag.PLAYER){
            entity.configure {
                it += Invulnarable(1f)
                it += Blink(1.5f,0.075f)
                it += Flash(color = Color.RED, weight = 0.75f, amount = 1, delay = 0.15f)
            }
            entity[State].stateMachine.changeState(GameObjectState.HIT)
        }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is EntityLifeChangeEvent ->{
                if (gameEvent.entity[Life].current != 0) {
                    audioService.play(SoundAsset.HURT)
                    return
                }
                audioService.play(SoundAsset.DEATH)
                gameEvent.entity.configure {
                    it -= EntityTag.PLAYER
                    it -= Move
                    it -= Life
                }
                GameEventDispatcher.fireEvent(PlayerDeathEvent)
            }
            else -> Unit
        }
    }

}


