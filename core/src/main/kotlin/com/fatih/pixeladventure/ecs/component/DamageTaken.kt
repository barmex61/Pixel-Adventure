package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class DamageTaken(var damageAmount : Int = 0,var isContinuous : Boolean = false) : Component <DamageTaken> {

    override fun type() = DamageTaken

    companion object : ComponentType<DamageTaken>()

}
