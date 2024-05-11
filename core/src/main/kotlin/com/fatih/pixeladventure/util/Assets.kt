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

    fun loadAll(){
        MapAsset.entries.forEach { assetManager.load<TiledMap>(it.path) }
        TextureAtlasAsset.entries.forEach { assetManager.load<TextureAtlas>(it.path) }
        assetManager.finishLoading()
    }

    operator fun get(textureAtlasAsset: TextureAtlasAsset) : TextureAtlas {
        return assetManager.get(textureAtlasAsset.path)
    }

    operator fun get(mapAsset: MapAsset) : TiledMap {
        return assetManager.get(mapAsset.path)
    }

    operator fun minus(mapAsset: MapAsset){
        assetManager.unload(mapAsset.path)

    }

    override fun dispose() {
        assetManager.disposeSafely()
    }


}
