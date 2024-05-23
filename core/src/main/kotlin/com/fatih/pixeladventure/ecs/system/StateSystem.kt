package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.game.PixelAdventure.Companion.isPhone
import com.github.quillraven.fleks.EachFrame
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class StateSystem : IteratingSystem(family = family { all(State) }) {


    override fun onTickEntity(entity: Entity) {
        entity[State].stateMachine.update()
    }


}
