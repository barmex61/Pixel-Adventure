package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ktx.assets.disposeSafely
import ktx.graphics.use

class RenderSystem(
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewport: Viewport = inject("gameViewport"),
    private val uiViewport: Viewport = inject("uiViewport"),
    private val uiStage : Stage = inject(),
    private val gameCamera : OrthographicCamera = inject()
) : IntervalSystem(), GameEventListener {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, PixelAdventure.UNIT_SCALE,spriteBatch).apply { setView(gameCamera) }
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val groundLayers = mutableListOf<TiledMapTileLayer>()
    private val entityComparator = compareEntityBy(Graphic)
    private val bgdEntities = family { all(Graphic, EntityTag.BACKGROUND) }
    private val fgdEntities = family { all(Graphic, EntityTag.FOREGROUND) }
    private val entities = family { all(Graphic).none(EntityTag.BACKGROUND,EntityTag.FOREGROUND) }

    override fun onTick() {
        gameViewport.apply()
        mapRenderer.setView(gameCamera)
        spriteBatch.use {
            backgroundLayers.forEach { mapRenderer.renderTileLayer(it) }
            renderEntities(bgdEntities)
            groundLayers.forEach {mapRenderer.renderTileLayer(it)}
            renderEntities(entities)
            renderEntities(fgdEntities)
        }
        uiViewport.apply()
        uiStage.act(deltaTime)
        uiStage.draw()
    }

    private fun renderEntities(family: Family) {
        family.sort(entityComparator)
        family.forEach {
            it[Graphic].sprite.draw(spriteBatch)
        }
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent ->{
                mapRenderer.map = gameEvent.tiledMap
                parseMapLayers(gameEvent.tiledMap)
            }
            else -> Unit
        }
    }

    private fun parseMapLayers(tiledMap: TiledMap){
        backgroundLayers.clear()
        groundLayers.clear()
        var currentLayers = backgroundLayers
        tiledMap.layers.forEach {layer->
            if (layer !is TiledMapTileLayer) return@forEach
            if (layer.name == "ground") currentLayers = groundLayers
            currentLayers += layer
        }
    }


    override fun onDispose() {
        mapRenderer.disposeSafely()
    }
}
