package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.ecs.system.MoveSystem.Companion.pow50outInterpolation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import kotlin.math.abs

class TrackSystem : IteratingSystem(family = World.family { all(Track,Move,Graphic) }) {

    override fun onTickEntity(entity: Entity) {
        val trackComp = entity[Track]
        val moveComp = entity[Move]
        val (trackPoints,currentTrackIx) = trackComp
        val (sprite) = entity[Graphic]

        moveComp.current = moveComp.max

        val currentX = sprite.x + sprite.width / 2f
        val currentY = sprite.y + sprite.height / 2f
        if (currentTrackIx == -1 || trackPoints[currentTrackIx].inRange(currentX,currentY)) {
            // entity reached current track point go to next point
            trackComp.currentTrackIx = (currentTrackIx + 1) % trackPoints.size
            val nextTrackPoint = trackPoints[trackComp.currentTrackIx]
            trackComp.angleRad = MathUtils.atan2( nextTrackPoint.y - currentY ,nextTrackPoint.x - currentX )
        }
        trackComp.moveX = moveComp.current * MathUtils.cos(trackComp.angleRad) * (abs(trackPoints[trackComp.currentTrackIx].x - currentX) * 0.4f )
        trackComp.moveY = moveComp.current * MathUtils.sin(trackComp.angleRad) * (abs(trackPoints[trackComp.currentTrackIx].y - currentY) * 0.4f )
    }

    private fun Vector2.inRange(otherX:Float, otherY:Float,tolerance : Float = 0.2f) : Boolean {
        return MathUtils.isEqual(this.x,otherX,tolerance) && MathUtils.isEqual(this.y,otherY,tolerance)
    }
}
