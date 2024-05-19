package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.fatih.pixeladventure.game.PhysicWorld
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.graphics.use

class PhysicDebugRenderSystem(
    private val physicWorld: PhysicWorld = inject(),
    private val gameCamera : OrthographicCamera = inject()
) : IntervalSystem() {

    private val b2dDebugRenderer = Box2DDebugRenderer().apply {
        SHAPE_STATIC.set(Color.RED)
    }

    private val shapeRenderer = ShapeRenderer()

    override fun onTick() {
        //b2dDebugRenderer.render(physicWorld,gameCamera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line,gameCamera){
            it.color = Color.RED
            it.rect(DEBUG_RECT.x, DEBUG_RECT.y, DEBUG_RECT.width, DEBUG_RECT.height)
        }
    }

    companion object{
        val DEBUG_RECT = Rectangle()
    }
}
