package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.MapAsset
import ktx.app.KtxScreen

class LoadingScreen(private val spriteBatch: SpriteBatch,private val assets: Assets) : KtxScreen {

    override fun show() {
        val tiledMap = assets[MapAsset.OBJECT]
        parseObjectCollisionShapes(tiledMap)
    }

    private fun parseObjectCollisionShapes(tiledMap: TiledMap){

    }
}
