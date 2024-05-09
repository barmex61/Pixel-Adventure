package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.Assets
import com.fatih.pixeladventure.GameEventDispatcher
import com.fatih.pixeladventure.GameEventDispatcher.fireEvent
import com.fatih.pixeladventure.GameEventListener
import com.fatih.pixeladventure.MapAsset
import com.fatih.pixeladventure.MapChangeEvent
import com.fatih.pixeladventure.PhysicWorld
import com.fatih.pixeladventure.ecs.system.GlProfilerSystem
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem
import com.fatih.pixeladventure.ecs.system.RenderSystem
import com.fatih.pixeladventure.ecs.system.SpawnSystem
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.earthGravity
import ktx.math.vec2

class GameScreen (spriteBatch: SpriteBatch,private val assets: Assets): KtxScreen {

    private val gameViewPort : FitViewport = FitViewport(10f,10f)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
    private val physicWorld : PhysicWorld = World(earthGravity,true)
    private val world = configureWorld {
        injectables {
            add("gameViewport",gameViewPort)
            add(gameCamera)
            add(assets)
            add(spriteBatch)
            add(physicWorld)
        }
        systems {
            add(SpawnSystem())
            add(RenderSystem())
            add(PhysicDebugRenderSystem())
            add(GlProfilerSystem())
        }
    }

    override fun show() {
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        val map = assets[MapAsset.MAP1]
        world.system<RenderSystem>().fireEvent(MapChangeEvent(map))
        world.system<SpawnSystem>().fireEvent(MapChangeEvent(map))
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
    }

    override fun dispose() {
        world.dispose()
        physicWorld.disposeSafely()
    }
}
