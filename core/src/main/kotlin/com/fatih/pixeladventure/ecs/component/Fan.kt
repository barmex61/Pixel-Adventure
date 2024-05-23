package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity


data class Fan(var collideEntity : Entity? = null,val rotation : Float ) : Component <Fan> {

    override fun type() = Fan

    companion object : ComponentType<Fan>()

}
