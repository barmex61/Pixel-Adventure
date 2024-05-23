package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.Gdx
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.game.PixelAdventure.Companion.isPhone
import com.github.quillraven.fleks.EachFrame
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class StateSystem : IteratingSystem(family = family { all(State) }, interval = if (isPhone) Fixed(1/300f) else EachFrame ) {

    init {
        STATE_DELTA_TIME = if (isPhone) 1/300f else Gdx.graphics.deltaTime
    }

    override fun onTickEntity(entity: Entity) {
        if (STATE_DELTA_TIME == 0f) STATE_DELTA_TIME = deltaTime
        entity[State].stateMachine.update()
    }

    companion object{
        var STATE_DELTA_TIME = 1/300f
    }

}
