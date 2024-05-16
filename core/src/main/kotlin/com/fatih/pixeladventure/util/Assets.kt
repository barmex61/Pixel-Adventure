package com.fatih.pixeladventure.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.ray3k.stripe.FreeTypeSkinLoader
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.assets.load
import java.util.logging.FileHandler

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
    JUMP("audio/jump.mp3"),
    HURT("audio/hurt.wav"),
    VICTORY("audio/victory.mp3"),
    COLLECT("audio/collect.wav")
}

enum class SkinAsset(val path : String){
    DEFAULT("ui/skin.json")
}

enum class ShaderAsset(val vertexShader : String,val fragmentShader : String){
    FLASH("shader/default.vert","shader/flash.frag")
}

class Assets : Disposable{

    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java,TmxMapLoader(fileHandleResolver))
        setLoader(Skin::class.java,FreeTypeSkinLoader(fileHandleResolver))
    }

    fun loadAll(){
        MapAsset.entries.forEach { assetManager.load<TiledMap>(it.path) }
        TextureAtlasAsset.entries.forEach { assetManager.load<TextureAtlas>(it.path) }
        SoundAsset.entries.forEach { assetManager.load<Sound>(it.path) }
        SkinAsset.entries.forEach { assetManager.load<Skin>(it.path) }
        ShaderAsset.entries.forEach {shaderAsset->
            assetManager.load<ShaderProgram>(shaderAsset.name,ShaderProgramParameter().apply {
                vertexFile = shaderAsset.vertexShader
                fragmentFile = shaderAsset.fragmentShader
            })
        }
        ShaderAsset.entries.forEach { shaderAsset ->
            assetManager.load<ShaderProgram>(shaderAsset.name, ShaderProgramParameter().apply {
                vertexFile = shaderAsset.vertexShader
                fragmentFile = shaderAsset.fragmentShader
            })
        }
        assetManager.finishLoading()

        // verify that all shaders compiled correctly
        val shaderErrors = ShaderAsset.entries
            .map { it to this[it] }
            .filterNot { (_, shader) -> shader.isCompiled }
            .map { (shaderAsset, failedShader) ->
                "Shader $shaderAsset failed to compile: ${failedShader.log}"
            }
        if (shaderErrors.isNotEmpty()) {
            gdxError("Shader compilation errors:\n ${shaderErrors.joinToString("\n\n\n")}")
        }

    }

    operator fun get(shaderAsset: ShaderAsset) : ShaderProgram {
        return assetManager.getAsset(shaderAsset.name)
    }

    operator fun plusAssign(musicAsset: MusicAsset) {
        assetManager.load<Music>(musicAsset.path)
        assetManager.finishLoading()
    }

    operator fun get(textureAtlasAsset: TextureAtlasAsset) : TextureAtlas {
        return assetManager.get(textureAtlasAsset.path)
    }

    operator fun get(mapAsset: MapAsset) : TiledMap {
        return assetManager.get(mapAsset.path)
    }

    operator fun get(skinAsset: SkinAsset) : Skin {
        return assetManager.get(skinAsset.path)
    }

    operator fun get(soundAsset: SoundAsset) : Sound {
        return assetManager.get(soundAsset.path)
    }

    operator fun get(musicAsset: MusicAsset) : Music {
        return assetManager.get(musicAsset.path)
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
