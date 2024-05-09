package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapLayers
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.pixeladventure.GameEvent
import com.fatih.pixeladventure.GameEventListener
import com.fatih.pixeladventure.MapChangeEvent
import com.fatih.pixeladventure.PixelAdventure
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely
import ktx.graphics.use

class RenderSystem(
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewport: FitViewport = inject("gameViewport"),
    private val gameCamera : OrthographicCamera = inject()
) : IntervalSystem() , GameEventListener{

    private val mapRenderer = OrthogonalTiledMapRenderer(null, PixelAdventure.UNIT_SCALE,spriteBatch).apply { setView(gameCamera) }
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val foregroundLayers = mutableListOf<TiledMapTileLayer>()

    override fun onTick() {
        gameViewport.apply()
        mapRenderer.setView(gameCamera)
        spriteBatch.use {
            backgroundLayers.forEach { mapRenderer.renderTileLayer(it) }
            //render entities
            foregroundLayers.forEach {mapRenderer.renderTileLayer(it)}
        }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent ->{
                mapRenderer.map = gameEvent.tiledMap
                parseMapLayers(gameEvent.tiledMap)
            }
        }
    }

    private fun parseMapLayers(tiledMap: TiledMap){
        backgroundLayers.clear()
        foregroundLayers.clear()
        var currentLayers = backgroundLayers
        tiledMap.layers.forEach {layer->
            when(layer){
                is TiledMapTileLayer -> currentLayers += layer
                is MapLayer -> currentLayers = foregroundLayers
            }
        }
    }


    override fun onDispose() {
        mapRenderer.disposeSafely()
    }
}
