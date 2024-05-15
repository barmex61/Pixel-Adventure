package com.fatih.pixeladventure.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils.*
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.ecs.component.Physic
import com.github.quillraven.fleks.Entity
import ktx.math.component1
import ktx.math.component2

private const val TOLERANCE_X = 0.1f
private const val TOLERANCE_Y = 1f
private const val ZERO = 0f

enum class GameObjectState : State<AiEntity>{

    IDLE{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: AiEntity) {
            val body = entity[Physic].body
            val (linX,linY) = body.linearVelocity
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                !isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RUN)
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
                isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(IDLE)
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
                isEqual(linY, ZERO, TOLERANCE_Y) ->{
                    if (isEqual(linX, ZERO, TOLERANCE_X)){
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
                isEqual(linY, ZERO, TOLERANCE_Y)  ->{
                    if (isEqual(linX, ZERO, TOLERANCE_X)){
                        entity.state(IDLE)
                    }else{
                        entity.state(RUN)
                    }
                }
            }
        }
    },
    ROCK_HEAD_IDLE{
        override fun enter(entity: AiEntity) {
            entity[Move].direction = MoveDirection.NONE
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: AiEntity) {
            entity.hasAggroTarget()?.let { aggroTarget->
                entity[Aggro].targetEntity = aggroTarget
                entity.state(ROCK_HEAD_AGRO)
            }
        }
    },
    ROCK_HEAD_AGRO{
        override fun enter(entity: AiEntity) {
            entity[Move].direction = MoveDirection.NONE
            entity.animation(AnimationType.AGGRO,Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            when{
                entity[Aggro].targetEntity == Entity.NONE -> entity.state(ROCK_HEAD_IDLE)
                entity.inRange(4.8f,entity[Aggro].targetEntity) -> entity.state(ROCK_HEAD_ATTACK)
            }
        }
    },
    ROCK_HEAD_ATTACK{
        override fun enter(entity: AiEntity) {
            if (entity[Aggro].aggroEntities.isEmpty()){
                entity.state(ROCK_HEAD_RETURN)
                return
            }
            entity.animation(AnimationType.RUN)
            entity.move(entity[Aggro].targetEntity)
        }

        override fun update(entity: AiEntity) {

            when{
                !entity.inRange(entity[Aggro].sourceLocation,4.5f) ->  entity.state(ROCK_HEAD_RETURN)
            }

        }
    },
    ROCK_HEAD_RETURN{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.RUN)
            val moveComp = entity[Move]
            moveComp.direction = moveComp.direction.opposite()
        }

        override fun update(entity: AiEntity) {
            if (entity.inRange(entity[Aggro].sourceLocation,0.2f)) entity.state(ROCK_HEAD_IDLE)
        }
    };

    override fun enter(entity: AiEntity) = Unit
    override fun update(entity: AiEntity) = Unit
    override fun exit(entity: AiEntity) = Unit
    override fun onMessage(entity: AiEntity, telegram: Telegram?) = false

}
