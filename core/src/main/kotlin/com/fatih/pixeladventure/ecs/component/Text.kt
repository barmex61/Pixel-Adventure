package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Rectangle
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Text(var text:String, val boundary : Rectangle) : Component <Text> {

    override fun type() = Text

    companion object : ComponentType<Text>()

}
