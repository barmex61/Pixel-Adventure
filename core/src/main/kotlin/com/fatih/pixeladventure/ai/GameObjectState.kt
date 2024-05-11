package com.fatih.pixeladventure.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.math.MathUtils.*
import com.fatih.pixeladventure.ecs.component.AnimationType
import ktx.math.component1
import ktx.math.component2

private const val TOLERANCE_X = 0.1f
private const val TOLERANCE_Y = 1f
private const val ZERO = 0f

sealed interface GameObjectState : State<AiEntity>{

    override fun enter(entity: AiEntity) = Unit
    override fun update(entity: AiEntity) = Unit
    override fun exit(entity: AiEntity) = Unit
    override fun onMessage(entity: AiEntity, telegram: Telegram?) = false
}

data object IdleState : GameObjectState{

    override fun enter(entity: AiEntity) {
        entity.animation(AnimationType.IDLE)
    }

    override fun update(entity: AiEntity) {
        val body = entity.physicComp.body
        val (linX,linY) = body.linearVelocity
        when{
            linY > TOLERANCE_Y -> entity.state(JumpState)
            linY < -TOLERANCE_Y -> entity.state(FallState)
            !isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RunState)
        }
    }
}

data object RunState : GameObjectState {
    override fun enter(entity: AiEntity) {
        entity.animation(AnimationType.RUN)
    }

    override fun update(entity: AiEntity) {
        val body = entity.physicComp.body
        val (linX,linY) = body.linearVelocity
        when{
            linY > TOLERANCE_Y -> entity.state(JumpState)
            linY < -TOLERANCE_Y -> entity.state(FallState)
            isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(IdleState)
        }
    }
}

data object JumpState : GameObjectState {
    override fun enter(entity: AiEntity) {
        entity.animation(AnimationType.JUMP)
    }

    override fun update(entity: AiEntity) {
        val body = entity.physicComp.body
        val (linX,linY) = body.linearVelocity
        when{
            linY < -TOLERANCE_Y -> entity.state(FallState)
            isEqual(linY, ZERO, TOLERANCE_Y) ->{
                if (isEqual(linX, ZERO, TOLERANCE_X)){
                    entity.state(IdleState)
                }else{
                    entity.state(RunState)
                }
            }
        }
    }
}

data object FallState : GameObjectState {
    override fun enter(entity: AiEntity) {
        entity.animation(AnimationType.FALL)
    }

    override fun update(entity: AiEntity) {
        val body = entity.physicComp.body
        val (linX,linY) = body.linearVelocity
        when{
            linY > TOLERANCE_Y -> entity.state(JumpState)
            isEqual(linY, ZERO, TOLERANCE_Y)  ->{
                if (isEqual(linX, ZERO, TOLERANCE_X)){
                    entity.state(IdleState)
                }else{
                    entity.state(RunState)
                }
            }
        }
    }
}
