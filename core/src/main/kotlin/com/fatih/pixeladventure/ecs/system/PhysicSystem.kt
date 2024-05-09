package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.game.PhysicWorld
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

class PhysicSystem(
    private val physicWorld: PhysicWorld = inject()
) : IteratingSystem(family = family {all(Physic)}, interval = Fixed(1/45f)) {

    override fun onUpdate() {
        if (physicWorld.autoClearForces){
            physicWorld.autoClearForces = false
        }
        super.onUpdate()
        physicWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicWorld.step(deltaTime,6,2)
    }

    override fun onTickEntity(entity: Entity) {
        val body = entity[Physic]
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val body = entity[Physic]
    }
}
