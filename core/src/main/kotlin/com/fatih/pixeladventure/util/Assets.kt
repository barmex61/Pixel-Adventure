package com.fatih.pixeladventure.util

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.fatih.pixeladventure.tiled.TiledLoader
import com.ray3k.stripe.FreeTypeSkinLoader
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.assets.load

enum class MapAsset(val path : String, val unlocksMap : MapAsset? = null, val parallaxBgdTexture : String = "graphics/green.png"){
    MAP25("map/map6.tmx",MAP25, parallaxBgdTexture = "graphics/purple.png"),
    MAP24("map/map6.tmx",MAP25,parallaxBgdTexture = "graphics/yellow.png"),
    MAP23("map/map6.tmx",MAP24, parallaxBgdTexture = "graphics/brown.png"),
    MAP22("map/map6.tmx",MAP23,parallaxBgdTexture = "graphics/blue.png"),
    MAP21("map/map6.tmx",MAP22, parallaxBgdTexture = "graphics/gray.png"),
    MAP20("map/map6.tmx",MAP21, parallaxBgdTexture = "graphics/purple.png"),
    MAP19("map/map6.tmx",MAP20,parallaxBgdTexture = "graphics/yellow.png"),
    MAP18("map/map6.tmx",MAP19, parallaxBgdTexture = "graphics/brown.png"),
    MAP17("map/map6.tmx",MAP18,parallaxBgdTexture = "graphics/blue.png"),
    MAP16("map/map6.tmx",MAP17, parallaxBgdTexture = "graphics/gray.png"),
    MAP15("map/map15.tmx",MAP16, parallaxBgdTexture = "graphics/purple.png"),
    MAP14("map/map14.tmx",MAP15,parallaxBgdTexture = "graphics/yellow.png"),
    MAP13("map/map13.tmx",MAP14, parallaxBgdTexture = "graphics/brown.png"),
    MAP12("map/map12.tmx",MAP13,parallaxBgdTexture = "graphics/blue.png"),
    MAP11("map/map11.tmx",MAP12, parallaxBgdTexture = "graphics/gray.png"),
    MAP10("map/map10.tmx",MAP11, parallaxBgdTexture = "graphics/purple.png"),
    MAP9("map/map9.tmx",MAP10,parallaxBgdTexture = "graphics/yellow.png"),
    MAP8("map/map8.tmx",MAP9, parallaxBgdTexture = "graphics/brown.png"),
    MAP7("map/map7.tmx",MAP8,parallaxBgdTexture = "graphics/blue.png"),
    MAP6("map/map6.tmx",MAP7, parallaxBgdTexture = "graphics/green.png"),
    MAP5("map/map5.tmx",MAP6, parallaxBgdTexture = "graphics/gray.png"),
    MAP4("map/map4.tmx",MAP5, parallaxBgdTexture = "graphics/purple.png"),
    MAP3("map/map3.tmx",MAP4,parallaxBgdTexture = "graphics/yellow.png"),
    MAP2("map/map2.tmx",MAP3, parallaxBgdTexture = "graphics/brown.png"),
    MAP1("map/map1.tmx",MAP2,parallaxBgdTexture = "graphics/blue.png"),
    TUTORIAL("map/tutorial.tmx",MAP1,parallaxBgdTexture = "graphics/pink.png"),
    OBJECT("map/object.tmx");
}

enum class TextureAtlasAsset(val path : String){
    GAMEOBJECT("graphics/gameObject.atlas")
}

enum class MusicAsset(val path : String){
    MUSIC1("audio/music1.wav"),
    MUSIC2("audio/music2.wav"),
    MUSIC3("audio/music3.wav"),
    MUSIC4("audio/music4.wav"),
    MUSIC5("audio/music5.wav"),
    MUSIC6("audio/music6.wav"),
    MUSIC7("audio/music7.wav"),
    MUSIC8("audio/music8.wav"),
    MUSIC9("audio/music9.wav"),
    MUSIC10("audio/music10.wav"),
    MUSIC11("audio/music11.wav"),
    MUSIC12("audio/music12.wav"),
    MUSIC13("audio/music13.wav"),
    MUSIC14("audio/music14.wav"),
    MUSIC15("audio/music15.wav"),
    MUSIC16("audio/music16.wav");
}

enum class SoundAsset(val path : String){
    JUMP("audio/jump.mp3"),
    HURT("audio/hurt.wav"),
    FLAG("audio/flag.wav"),
    COLLECT("audio/collect.wav"),
    PAUSE("audio/pause.wav"),
    DEATH("audio/death.wav")
}

enum class SkinAsset(val path : String){
    DEFAULT("ui/skin.json")
}

enum class ShaderAsset(val vertexShader : String,val fragmentShader : String){
    FLASH("shader/default.vert","shader/flash.frag")
}

class Assets : Disposable{

    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java,TiledLoader(fileHandleResolver))
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
