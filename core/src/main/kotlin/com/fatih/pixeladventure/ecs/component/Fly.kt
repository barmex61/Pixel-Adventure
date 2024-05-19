package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Fly(var timer : Float = 2f) : Component <Fly> {

    override fun type() = Fly

    companion object : ComponentType<Fly>()

}
