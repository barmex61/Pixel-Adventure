package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
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
import com.fatih.pixeladventure.ecs.system.RenderSystem
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen

class GameScreen (spriteBatch: SpriteBatch,private val assets: Assets): KtxScreen {

    private val gameViewPort : FitViewport = FitViewport(10f,10f)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
    private val world = configureWorld {
        injectables {
            add("gameViewport",gameViewPort)
            add(gameCamera)
            add(assets)
            add(spriteBatch)
        }
        systems {
            add(RenderSystem())
        }
    }

    override fun show() {
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        val map = assets[MapAsset.MAP1]
        world.system<RenderSystem>().fireEvent(MapChangeEvent(map))
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
    }

    override fun dispose() {
        world.dispose()
    }
}
