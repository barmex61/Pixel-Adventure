package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.github.quillraven.fleks.Entity


enum class RockHeadState : EntityState{

    ROCK_HEAD_IDLE{
        override fun enter(entity: AiEntity) {
            entity[Move].direction = MoveDirection.NONE
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: AiEntity) {
            entity.hasAggroTarget()?.let { aggroTarget->
                if (entity.isPathBlocked(aggroTarget)){
                    return
                }
                entity[Aggro].targetEntity = aggroTarget
                entity.state(ROCK_HEAD_AGRO)
            }
        }
    },

    ROCK_HEAD_AGRO{
        override fun enter(entity: AiEntity) {
            entity[Move].direction = MoveDirection.NONE
            entity.animation(AnimationType.AGGRO, Animation.PlayMode.NORMAL)
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
                entity[Aggro].targetEntity == Entity.NONE -> entity.state(ROCK_HEAD_RETURN)
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
}
