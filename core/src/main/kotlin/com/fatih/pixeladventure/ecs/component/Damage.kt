package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Damage(var amount : Int) : Component <Damage> {

    override fun type() = Damage

    companion object : ComponentType<Damage>()

}
