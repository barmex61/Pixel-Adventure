package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.ShaderAsset
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.propertyOrNull
import ktx.tiled.rotation
import ktx.tiled.shape

class RenderSystem(
    private val batch: SpriteBatch = inject(),
    private val gameViewport: Viewport = inject("gameViewport"),
    private val uiViewport: Viewport = inject("uiViewport"),
    private val uiStage : Stage = inject(),
    private val gameCamera : OrthographicCamera = inject(),
    assets: Assets = inject()
) : IntervalSystem(), GameEventListener {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, PixelAdventure.UNIT_SCALE,batch).apply { setView(gameCamera) }
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val groundLayers = mutableListOf<TiledMapTileLayer>()
    private val entityComparator = compareEntityBy(Graphic)
    private val bgdEntities = family { all(Graphic, EntityTag.BACKGROUND) }
    private val fgdEntities = family { all(Graphic, EntityTag.FOREGROUND) }
    private val entities = family { all(Graphic).none(EntityTag.BACKGROUND,EntityTag.FOREGROUND) }
    private val flashShader = assets[ShaderAsset.FLASH]
    private val uLocFlashColor = flashShader.getUniformLocation("u_FlashColor")
    private val uLocFlashWeight = flashShader.getUniformLocation("u_FlashWeight")


    override fun onTick() {
        gameViewport.apply()
        mapRenderer.setView(gameCamera)
        batch.use {
            backgroundLayers.forEach { mapRenderer.renderTileLayer(it) }
            bgdEntities.renderEntities()
            groundLayers.forEach {mapRenderer.renderTileLayer(it)}
            entities.renderEntities()
            fgdEntities.renderEntities()
        }
        uiViewport.apply()
        uiStage.act(deltaTime)
        uiStage.draw()
    }

    private fun Family.renderEntities() {
        sort(entityComparator)
        forEach { entity ->
            val flashCmp = entity.getOrNull(Flash)
            if (flashCmp != null && flashCmp.doFlash) {
                if (batch.shader != flashShader) {
                    batch.shader = flashShader
                }
                flashShader.use {
                    flashShader.setUniformf(uLocFlashColor, flashCmp.color)
                    flashShader.setUniformf(uLocFlashWeight, flashCmp.weight)
                }
            } else {
                batch.resetShader()
            }
            entity[Graphic].sprite.draw(batch)
        }

        batch.resetShader()
    }

    private fun Batch.resetShader() {
        if (shader != null) {
            shader = null
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
