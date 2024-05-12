package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.PixelAdventure.Companion.OBJECT_FIXTURES
import com.fatih.pixeladventure.tiled.TiledService.Companion.fixtureDefinitionOf
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.GameProperties
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.util.SkinAsset
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.scene2d.Scene2DSkin
import ktx.tiled.propertyOrNull

class LoadingScreen(
    private val spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val pixelAdventure: PixelAdventure,
    private val assets: Assets,
    private val audioService: AudioService,
    private val gameProperties: GameProperties
) : KtxScreen {

    override fun show() {
        assets.loadAll()
        val tiledMap = assets[MapAsset.OBJECT]
        parseObjectCollisionShapes(tiledMap)
        assets -= MapAsset.OBJECT
        Scene2DSkin.defaultSkin = assets[SkinAsset.DEFAULT]
        pixelAdventure.removeScreen<LoadingScreen>()
        dispose()
        pixelAdventure.addScreen(GameScreen(spriteBatch,physicWorld,assets,audioService,gameProperties))
        pixelAdventure.setScreen<GameScreen>()
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
