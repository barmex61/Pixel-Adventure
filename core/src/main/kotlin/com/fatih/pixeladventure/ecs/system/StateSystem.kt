package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.Gdx
import com.fatih.pixeladventure.ecs.component.State
import com.github.quillraven.fleks.EachFrame
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class StateSystem : IteratingSystem(family = family { all(State) }, interval = if (Gdx.graphics.deltaTime < 1/300f) EachFrame else Fixed(1/300f) ) {


    override fun onTickEntity(entity: Entity) {
        entity[State].stateMachine.update()
    }

}
