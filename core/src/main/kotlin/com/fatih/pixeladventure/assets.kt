package com.fatih.pixeladventure

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import ktx.assets.disposeSafely
import ktx.assets.load

enum class MapAsset(val path : String){
    MAP1("map/map1.tmx")
}

class Assets {

    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java,TmxMapLoader())
    }

    operator fun get(mapAsset: MapAsset) : TiledMap {
        assetManager.load<TiledMap>(mapAsset.path)
        assetManager.finishLoading()
        return assetManager.get(mapAsset.path)
    }
    fun dispose(){
        assetManager.disposeSafely()
    }
}
