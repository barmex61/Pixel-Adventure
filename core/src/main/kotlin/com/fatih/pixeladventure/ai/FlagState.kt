package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.fatih.pixeladventure.ai.EntityState.Companion.TOLERANCE_X
import com.fatih.pixeladventure.ai.EntityState.Companion.TOLERANCE_Y
import com.fatih.pixeladventure.ai.EntityState.Companion.ZERO
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic
import ktx.math.component1
import ktx.math.component2

enum class FlagState : EntityState{
    START{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.START,Animation.PlayMode.LOOP)
        }
    },

    IDLE{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.IDLE,Animation.PlayMode.LOOP)
        }

    },

    RUN{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.RUN,Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) entity.state(WAVE)
        }
    },

    WAVE{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.WAVE,Animation.PlayMode.LOOP)
        }
    }


}
