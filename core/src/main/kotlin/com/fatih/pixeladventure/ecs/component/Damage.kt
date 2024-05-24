package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Damage(var damage : Int = 1) : Component <Damage> {

    override fun type() = Damage

    companion object : ComponentType<Damage>()

}
