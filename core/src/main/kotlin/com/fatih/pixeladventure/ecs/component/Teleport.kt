package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Teleport(val startLocation : Vector2,var doTeleport : Boolean = false ) : Component <Teleport> {

    override fun type() = Teleport

    companion object : ComponentType<Teleport>()

}
