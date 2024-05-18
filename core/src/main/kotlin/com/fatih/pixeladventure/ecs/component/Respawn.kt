package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Respawn(var duration: Float, val entityTag: EntityTag) : Component <Respawn> {

    override fun type() = Respawn

    companion object : ComponentType<Respawn>()

}
