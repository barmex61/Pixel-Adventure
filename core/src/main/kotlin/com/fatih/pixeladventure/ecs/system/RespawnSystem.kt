package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.fatih.pixeladventure.ecs.component.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Remove
import com.fatih.pixeladventure.ecs.component.Respawn
import com.fatih.pixeladventure.util.animation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class RespawnSystem : IteratingSystem(family = World.family { all(Respawn).none(Remove) }){

    override fun onTickEntity(entity: Entity) {
        val respawnComp = entity[Respawn]
        with(respawnComp){
            duration -= deltaTime
            if (duration <= 0f){
                entity.configure {
                    it -= Respawn
                    it += this@with.entityTag
                    world.animation(it,AnimationType.HIT,PlayMode.REVERSED)
                    it[Animation].apply {
                        nextAnimation = AnimationType.IDLE
                        playMode = PlayMode.LOOP
                    }
                }
            }
        }
    }
}
