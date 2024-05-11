package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.util.animation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class AiEntity(val entity: Entity,val world: World) {

    val physicComp : Physic = with(world){entity[Physic]}

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP) = world.animation(entity,animationType,playMode)

    fun state(state : GameObjectState){
        with(world){entity[State]}.stateMachine.changeState(state)
    }
}
