package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ktx.assets.disposeSafely
import ktx.graphics.use

class RenderSystem(
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewport: Viewport = inject("gameViewport"),
    private val gameCamera : OrthographicCamera = inject()
) : IteratingSystem(
    family = family { all(Graphic) },
    comparator = compareEntityBy(Graphic)
), GameEventListener {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, PixelAdventure.UNIT_SCALE,spriteBatch).apply { setView(gameCamera) }
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val foregroundLayers = mutableListOf<TiledMapTileLayer>()

    override fun onTick() {
        gameViewport.apply()
        mapRenderer.setView(gameCamera)
        spriteBatch.use {
            backgroundLayers.forEach { mapRenderer.renderTileLayer(it) }
            //render entities
            super.onTick()
            foregroundLayers.forEach {mapRenderer.renderTileLayer(it)}
        }
    }

    override fun onTickEntity(entity: Entity) {
        val (sprite) = entity[Graphic]
        sprite.draw(spriteBatch)
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
