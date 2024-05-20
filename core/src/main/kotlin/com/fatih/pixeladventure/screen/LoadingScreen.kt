package com.fatih.pixeladventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.PixelAdventure.Companion.OBJECT_FIXTURES
import com.fatih.pixeladventure.tiled.TiledService.Companion.fixtureDefinitionOf
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.GameProperties
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.util.MusicAsset
import com.fatih.pixeladventure.util.SkinAsset
import ktx.actors.plusAssign
import ktx.actors.then
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.tiled.propertyOrNull


class LoadingScreen(
    private val spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val pixelAdventure: PixelAdventure,
    private val assets: Assets,
    private val audioService: AudioService,
    private val gameProperties: GameProperties,
    private val gamePreferences: GamePreferences
) : KtxScreen {

    private val viewPort = StretchViewport(480f,270f)
    private val loadingStage = Stage(viewPort,spriteBatch)

    override fun show() {
        assets.loadAll()
        val tiledMap = assets[MapAsset.OBJECT]
        parseObjectCollisionShapes(tiledMap)
        assets -= MapAsset.OBJECT
        Scene2DSkin.defaultSkin = assets[SkinAsset.DEFAULT]
        audioService.play(MusicAsset.MUSIC6)
        loadingStage.actors {
            table {
                setFillParent(true)
                label("Press any key to continue ...","loading_label"){
                    this += forever(fadeOut(1f) then fadeIn(1f))
                }
            }
        }
    }

    override fun render(delta: Float) {
        viewPort.apply()
        loadingStage.act(delta)
        loadingStage.draw()
        if (Gdx.input.justTouched()){
            pixelAdventure.removeScreen<LoadingScreen>()
            dispose()
            pixelAdventure.addScreen(MenuScreen(spriteBatch,audioService,pixelAdventure,gamePreferences))
            pixelAdventure.addScreen(GameScreen(spriteBatch,physicWorld,assets,audioService,gameProperties,pixelAdventure,gamePreferences))
            pixelAdventure.setScreen<MenuScreen>()
        }
    }

    override fun resize(width: Int, height: Int) {
        loadingStage.viewport.update(width,height)
    }

    private fun parseObjectCollisionShapes(tiledMap: TiledMap){
        val tileset = tiledMap.tileSets.getTileSet(0) ?: gdxError("There is no tileset in the ${MapAsset.OBJECT} tiledMap")
        val firstGid = tileset.propertyOrNull<Int>("firstgid")?: gdxError("Tileset $tileset does not have 'firstgid' property" )
        for (i in 0 until  tileset.size()){
            val tileId = firstGid + i
            val tile = tileset.getTile(tileId)
            val objectFixtureDefinitions = tile.objects.map { fixtureDefinitionOf(it) }
            if (objectFixtureDefinitions.isEmpty()) gdxError("No collision shapes defined for tile ${tile.id}")
            val gameObjectStr : String = tile.propertyOrNull("gameObject") ?: gdxError("Missing property 'GameObject' on tile ${tile.id} ")
            OBJECT_FIXTURES[GameObject.valueOf(gameObjectStr)] = objectFixtureDefinitions
        }
    }

}
