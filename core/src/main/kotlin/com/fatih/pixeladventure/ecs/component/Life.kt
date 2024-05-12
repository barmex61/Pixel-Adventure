package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Life(var current : Int , var max : Int) : Component <Life> {

    constructor(max : Int) : this(max,max)

    override fun type() = Life

    companion object : ComponentType<Life>()

}
