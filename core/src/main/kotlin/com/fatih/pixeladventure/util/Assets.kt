package com.fatih.pixeladventure.util

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Disposable
import ktx.assets.disposeSafely
import ktx.assets.load

enum class MapAsset(val path : String){
    TEST("map/test.tmx"),
    OBJECT("map/object.tmx")
}

enum class TextureAtlasAsset(val path : String){
    GAMEOBJECT("graphics/gameObject.atlas")
}

class Assets : Disposable{

    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java,TmxMapLoader())
    }

    operator fun get(textureAtlasAsset: TextureAtlasAsset) : TextureAtlas {
        assetManager.load<TextureAtlas>(textureAtlasAsset.path)
        assetManager.finishLoading()
        return assetManager.get(textureAtlasAsset.path)
    }

    operator fun get(mapAsset: MapAsset) : TiledMap {
        assetManager.load<TiledMap>(mapAsset.path)
        assetManager.finishLoading()
        return assetManager.get(mapAsset.path)
    }

    operator fun minus(mapAsset: MapAsset){
        assetManager.unload(mapAsset.path)

    }

    override fun dispose() {
        assetManager.disposeSafely()
    }


}
