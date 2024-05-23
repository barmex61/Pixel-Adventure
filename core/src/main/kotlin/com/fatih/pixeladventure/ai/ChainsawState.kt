package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.fatih.pixeladventure.ai.EntityState.Companion.TOLERANCE_X
import com.fatih.pixeladventure.ai.EntityState.Companion.TOLERANCE_Y
import com.fatih.pixeladventure.ai.EntityState.Companion.ZERO
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Track
import ktx.math.component1
import ktx.math.component2

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
