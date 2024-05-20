package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType


enum class TrambolineState : EntityState {
    OFF{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.OFF)
        }

    },
    ON{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ON,Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {

        }
    }
}
