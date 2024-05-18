package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.fatih.pixeladventure.ai.GameObjectState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Teleport
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class TeleportSystem (
    private val audioService: AudioService = World.inject()
): IteratingSystem(family = World.family { all(Teleport) }) {

    override fun onTickEntity(entity: Entity) {
        val teleportComp = entity[Teleport]
        val (spawnLocation,doTeleport) = teleportComp
        if (!doTeleport) return
        val physicComp = entity[Physic]
        val lifeComp = entity[Life]
        lifeComp.current = (lifeComp.current -1).coerceAtLeast(0)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(entity))
        entity.configure {
            it += Blink(1.5f,0.075f)
            it +=  Flash(color = Color.RED, weight = 0.75f, amount = 1, delay = 0.15f)
            entity[State].stateMachine.changeState(GameObjectState.HIT)
            audioService.play(SoundAsset.HURT)
        }
        physicComp.body.setTransform(spawnLocation,0f)
        teleportComp.doTeleport = false
    }
}
