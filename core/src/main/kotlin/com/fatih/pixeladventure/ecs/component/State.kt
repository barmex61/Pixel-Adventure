package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.fatih.pixeladventure.ai.AiEntity
import com.fatih.pixeladventure.ai.EntityState
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

typealias AiState = com.badlogic.gdx.ai.fsm.State<AiEntity>

data class State(
    val owner : AiEntity,
    val initialState : EntityState,
    val stateMachine : DefaultStateMachine<AiEntity,AiState> = DefaultStateMachine(owner).apply { changeState(initialState) }
) : Component <State> {

    override fun type() = State

    companion object : ComponentType<State>()

}
