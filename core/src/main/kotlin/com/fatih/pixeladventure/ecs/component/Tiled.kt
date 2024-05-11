package com.fatih.pixeladventure.ecs.component

import com.fatih.pixeladventure.util.GameObject
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Tiled(val mapObjectId : Int , val gameObject: GameObject) : Component <Tiled> {

    override fun type() = Tiled

    companion object : ComponentType<Tiled>()

}
