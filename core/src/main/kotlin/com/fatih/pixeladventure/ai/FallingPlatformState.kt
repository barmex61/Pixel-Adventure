package com.fatih.pixeladventure.ai

import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.system.StateSystem
import com.fatih.pixeladventure.game.PixelAdventure
import kotlin.math.abs

enum class FallingPlatformState : EntityState {
    ON{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ON)
        }

    },
    OFF{
        override fun enter(entity: AiEntity) {
            entity.entityRemoveDuration = 1.5f
        }

        override fun update(entity: AiEntity) {
            entity.entityRemoveDuration -= entity.world.deltaTime
            if (entity.entityRemoveDuration <= 0.75f ){
                if (entity.animationType != AnimationType.OFF) entity.animation(AnimationType.OFF)
                with(entity.world){
                    entity.entity[Physic].body.setLinearVelocity(0f,-7.5f * abs(1f/entity.entityRemoveDuration))
                }
            }
            if (entity.entityRemoveDuration <= 0f){
                entity.entityRemoveDuration = 1.5f
                entity.remove()
            }
        }
    }
}
