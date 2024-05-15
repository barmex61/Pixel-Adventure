package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Teleport
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class TeleportSystem : IteratingSystem(family = World.family { all(Teleport) }) {

    override fun onTickEntity(entity: Entity) {
        val teleportComp = entity[Teleport]
        val (spawnLocation,doTeleport) = teleportComp
        if (!doTeleport) return
        val physicComp = entity[Physic]
        val lifeComp = entity[Life]
        lifeComp.current = (lifeComp.current -1).coerceAtLeast(0)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(entity))
        physicComp.body.setTransform(spawnLocation,0f)
        teleportComp.doTeleport = false
    }
}
