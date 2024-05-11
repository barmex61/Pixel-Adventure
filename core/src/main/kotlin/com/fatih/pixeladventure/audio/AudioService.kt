package com.fatih.pixeladventure.audio

import com.badlogic.gdx.audio.Music
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.MusicAsset
import com.fatih.pixeladventure.util.SoundAsset
import ktx.tiled.property
import ktx.tiled.propertyOrNull

private data class MusicResource(val music: Music, val musicAsset: MusicAsset)

class AudioService(private val assets: Assets,private var soundVolume : Float = 1f , private var musicVolume : Float = 1f)
    : GameEventListener{

    init {
        GameEventDispatcher.register(this)
    }

    private val soundQueue = mutableSetOf<SoundAsset>()
    private var currentMusicResource : MusicResource? = null

    fun play(soundAsset: SoundAsset){
        soundQueue += soundAsset
    }

    fun play(musicAsset: MusicAsset){
        if (currentMusicResource?.musicAsset == musicAsset){
            return
        }
        currentMusicResource?.let {
            it.music.stop()
            assets -= it.musicAsset
        }
        assets += musicAsset
        currentMusicResource = MusicResource(assets[musicAsset],musicAsset)
        currentMusicResource!!.music.apply {
            volume = musicVolume
            play()
        }

    }

    fun update(){
        soundQueue.forEach { soundAsset ->
            assets[soundAsset].play(soundVolume)
        }
        soundQueue.clear()
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent ->{
                gameEvent.tiledMap.propertyOrNull<String>("musicAsset")?.let {
                    play(MusicAsset.valueOf(it))
                    println()
                }
            }
        }
    }
}
