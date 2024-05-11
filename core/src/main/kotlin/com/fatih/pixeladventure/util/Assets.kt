package com.fatih.pixeladventure.util

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
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

enum class MusicAsset(val path : String){
    TUTORIAL("audio/tutorial.mp3")
}

enum class SoundAsset(val path : String){
    JUMP("audio/jump.mp3")
}

class Assets : Disposable{

    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java,TmxMapLoader())
    }

    fun loadAll(){
        MapAsset.entries.forEach { assetManager.load<TiledMap>(it.path) }
        TextureAtlasAsset.entries.forEach { assetManager.load<TextureAtlas>(it.path) }
        SoundAsset.entries.forEach { assetManager.load<Sound>(it.path) }
        assetManager.finishLoading()
    }


    operator fun get(textureAtlasAsset: TextureAtlasAsset) : TextureAtlas {
        return assetManager.get(textureAtlasAsset.path)
    }

    operator fun get(mapAsset: MapAsset) : TiledMap {
        return assetManager.get(mapAsset.path)
    }

    operator fun get(soundAsset: SoundAsset) : Sound {
        return assetManager.get(soundAsset.path)
    }

    operator fun get(musicAsset: MusicAsset) : Music {
        return assetManager.get(musicAsset.path)
    }

    operator fun plusAssign(musicAsset: MusicAsset) {
        assetManager.load<Music>(musicAsset.path)
        assetManager.finishLoading()
    }

    operator fun minusAssign(mapAsset: MapAsset){
        assetManager.unload(mapAsset.path)
    }

    operator fun minusAssign(musicAsset: MusicAsset){
        assetManager.unload(musicAsset.path)
    }

    override fun dispose() {
        assetManager.disposeSafely()
    }


}
