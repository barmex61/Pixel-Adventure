package com.fatih.pixeladventure.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.g2d.Animation
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.github.quillraven.fleks.Entity


interface EntityState : State<AiEntity>{
    companion object{
        const val TOLERANCE_X = 0.25f
        const val TOLERANCE_Y = 1f
        const val ZERO = 0f
    }
    override fun enter(entity: AiEntity) = Unit
    override fun update(entity: AiEntity) = Unit
    override fun exit(entity: AiEntity) = Unit
    override fun onMessage(entity: AiEntity?, telegram: Telegram?) = false
}


