package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import ktx.tiled.height
import ktx.tiled.width
import kotlin.math.max

class CameraSystem (
    private val gameCamera : OrthographicCamera = World.inject()
): IteratingSystem(family = family { all(Graphic,EntityTag.CAMERA_FOCUS) }) , GameEventListener {

    private val mapBoundaries = Vector2(0f,0f)

    override fun onTickEntity(entity: Entity) {
        val (sprite) = entity[Graphic]

        var camX = sprite.x + sprite.width * 0.5f
        var camY = sprite.y + sprite.height * 0.5f
        if (!mapBoundaries.isZero){
            val viewportW = gameCamera.viewportWidth/2f
            val viewportH = gameCamera.viewportHeight/2f
            camX = camX.coerceIn(viewportW, max(mapBoundaries.x - viewportW,viewportW) )
            camY = camY.coerceIn(viewportH, max(mapBoundaries.y - viewportH,viewportH) )
        }
        gameCamera.position.set(camX,camY,0f)
        gameCamera.update()
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent -> {
                mapBoundaries.set(
                    gameEvent.tiledMap.width.toFloat() , gameEvent.tiledMap.height.toFloat()
                )
            }
            else -> Unit
        }
    }
}
