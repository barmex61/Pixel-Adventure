package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Remove(var duration : Float, var removePhysic : Boolean = false) : Component <Remove> {

    override fun type() = Remove

    companion object : ComponentType<Remove>()

}
