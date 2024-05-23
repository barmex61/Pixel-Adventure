package com.fatih.pixeladventure.ai

import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Fan
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.system.StateSystem
import com.fatih.pixeladventure.game.PixelAdventure
import kotlin.math.cos
import kotlin.math.sin

enum class FanPlatformState : EntityState {
    ON{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ON)
        }

        override fun update(entity: AiEntity) {
            entity.hasFanTarget()?.let {
                val rotation = 90 - entity[Fan].rotation
                with(entity.world){
                    val playerBody = it[Physic].body
                    playerBody.setLinearVelocity(playerBody.linearVelocity.x,playerBody.linearVelocity.y +  deltaTime * 50f * sin(rotation))
                }
            }
        }

    },

}
