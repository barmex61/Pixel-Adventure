package com.fatih.pixeladventure.ecs.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Track(val trackPoint : List<Vector2>, var currentTrackIx : Int = -1,var moveX : Float = 0f, var moveY : Float = 0f,var angleRad : Float = 0f) : Component <Track> {

    override fun type() = Track

    companion object : ComponentType<Track>()

}
