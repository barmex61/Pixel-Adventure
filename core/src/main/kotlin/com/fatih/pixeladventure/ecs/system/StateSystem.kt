package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.State
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class StateSystem : IteratingSystem(family = World.family { all(State) }) {

    override fun onTickEntity(entity: Entity) {
        entity[State].stateMachine.update()
    }

}
