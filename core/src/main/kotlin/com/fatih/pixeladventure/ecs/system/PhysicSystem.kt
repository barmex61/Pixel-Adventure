package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.game.PhysicWorld
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.math.component1
import ktx.math.component2

class PhysicSystem(
    private val physicWorld: PhysicWorld = inject(),
    interval : Interval = Fixed(1/300f)
) : IteratingSystem(family = family {all(Physic,Graphic)}, interval = interval) {

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
        val (body,previousPosition) = entity[Physic]
        previousPosition.set(body.position)
        entity.getOrNull(Move)?.let {moveComp ->
            body.setLinearVelocity(moveComp.current , body.linearVelocity.y)
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val (sprite) = entity[Graphic]
        val (body,previousPosition) = entity[Physic]
        val (prevX,prevY) = previousPosition
        val (bodyX,bodyY) = body.position
        sprite.setPosition(
            MathUtils.lerp(prevX,bodyX,alpha),
            MathUtils.lerp(prevY,bodyY,alpha)
        )
    }
}
