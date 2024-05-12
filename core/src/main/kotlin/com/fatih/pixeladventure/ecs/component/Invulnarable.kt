package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Invulnarable(var time : Float) : Component <Invulnarable> {

    override fun type() = Invulnarable

    companion object : ComponentType<Invulnarable>()

}
