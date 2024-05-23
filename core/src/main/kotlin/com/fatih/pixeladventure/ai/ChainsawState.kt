package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType


enum class ChainsawState : EntityState{
    FOLLOW_TRACK{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.ON,Animation.PlayMode.LOOP)
        }

        override fun update(entity: AiEntity) {
            if (entity.hasTrack) entity.followTrack(fixedVelocity = true)
        }
    },

}
