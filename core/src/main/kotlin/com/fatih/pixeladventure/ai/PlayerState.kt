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

enum class PlayerState : EntityState{
    IDLE{
        override fun enter(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RUN)
                else ->  entity.animation(AnimationType.IDLE)
            }
        }

        override fun update(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RUN)
            }
        }
    },

    RUN {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.RUN)
        }

        override fun update(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(IDLE)
            }
        }
    },

    JUMP {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.JUMP)
        }

        override fun update(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY < -TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linY, ZERO, TOLERANCE_Y) ->{
                    if (MathUtils.isEqual(linX, ZERO, TOLERANCE_X)){
                        entity.state(IDLE)
                    }else{
                        entity.state(RUN)
                    }
                }
            }
        }
    },

    FALL {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.FALL)
        }

        override fun update(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                MathUtils.isEqual(linY, ZERO, TOLERANCE_Y) ->{
                    if (MathUtils.isEqual(linX, ZERO, TOLERANCE_X)){
                        entity.state(IDLE)
                    }else{
                        entity.state(RUN)
                    }
                }
            }
        }
    },

    DOUBLE_JUMP{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.DOUBLE_JUMP, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) entity.state(IDLE)
        }
    },

    HIT {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.HIT, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) entity.state(IDLE)
        }
    },
}
