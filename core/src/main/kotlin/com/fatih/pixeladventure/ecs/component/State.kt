package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.StateMachine
import com.fatih.pixeladventure.ai.AiEntity
import com.fatih.pixeladventure.ai.GameObjectState
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class State(
    val owner : AiEntity,
    val initialState : GameObjectState,
    val stateMachine : StateMachine<AiEntity,GameObjectState> = DefaultStateMachine(owner, initialState)
) : Component <State> {

    override fun type() = State

    companion object : ComponentType<State>()

}
