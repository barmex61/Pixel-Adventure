package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Collectable(val name : String) : Component <Collectable> {

    override fun type() = Collectable

    companion object : ComponentType<Collectable>()

}
