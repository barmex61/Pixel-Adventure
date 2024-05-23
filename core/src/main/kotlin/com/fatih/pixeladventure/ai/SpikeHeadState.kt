package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.fatih.pixeladventure.ecs.component.Animation.Companion
import com.fatih.pixeladventure.ecs.component.AnimationType


enum class SpikeHeadState : EntityState {


    AGGRO{


        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()){
                if (entity.hasTrack) entity.followTrack(fixedVelocity = true, stopOnTrackChange = true)
                if (entity.changeTrackPosition) entity.state(HIT)
            }

        }

    },

    HIT{
        override fun enter(entity: AiEntity) {
            entity.animation(entity.currentAnimType)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) {
                entity.currentAnimType = entity.nextAnimType
                entity.state(AGGRO)
                entity.animation(AnimationType.AGGRO,Animation.PlayMode.NORMAL)
                entity.changeTrackPosition = false
            }
        }
    }

}
