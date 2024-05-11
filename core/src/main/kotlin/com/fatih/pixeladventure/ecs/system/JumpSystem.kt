package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import kotlin.math.sqrt

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
    private val audioService: AudioService = World.inject()
): IteratingSystem(family = World.family { all(Jump) }) {

    override fun onTickEntity(entity: Entity) {
        val jumpComps = entity[Jump]
        val (body,_) = entity[Physic]
        val (maxHeight , buffer) = entity[Jump]
        if (buffer == 0f ){
            return
        }
        jumpComps.buffer = 0f
        val gravityY = if (physicWorld.gravity.y == 0f) 1f else physicWorld.gravity.y
        body.setLinearVelocity(body.linearVelocity.x, sqrt(2 * maxHeight * -gravityY))
        audioService.play(SoundAsset.JUMP)
    }
}
