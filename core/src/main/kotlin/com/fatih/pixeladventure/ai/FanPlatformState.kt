package com.fatih.pixeladventure.ai

import com.badlogic.gdx.Gdx
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic

enum class FanPlatformState : EntityState {
    ON{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ON)
        }

        override fun update(entity: AiEntity) {
            entity.hasFanTarget()?.let {
                with(entity.world){
                    val playerBody = it[Physic].body
                    println(playerBody.linearVelocity.y)
                    playerBody.setLinearVelocity(playerBody.linearVelocity.x,playerBody.linearVelocity.y + 60f/ Gdx.graphics.framesPerSecond)
                }
            }
        }

    },

}
