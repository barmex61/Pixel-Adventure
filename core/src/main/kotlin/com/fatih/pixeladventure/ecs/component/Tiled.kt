package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Rectangle
import com.fatih.pixeladventure.util.GameObject
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Tiled(val mapObjectId : Int , val gameObject: GameObject,val mapObjectBoundary : Rectangle = Rectangle()) : Component <Tiled> {

    override fun type() = Tiled

    companion object : ComponentType<Tiled>()

}
