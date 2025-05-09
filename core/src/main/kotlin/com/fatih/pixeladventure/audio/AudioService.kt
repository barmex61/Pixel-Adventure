package com.fatih.pixeladventure.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.fatih.pixeladventure.event.CollectItemEvent
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MainMenuEvent
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.event.MuteAudioEvent
import com.fatih.pixeladventure.event.PlaySoundEvent
import com.fatih.pixeladventure.event.VictoryEvent
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.MusicAsset
import com.fatih.pixeladventure.util.SoundAsset
import ktx.tiled.property
import ktx.tiled.propertyOrNull

private data class MusicResource(val music: Music, val musicAsset: MusicAsset)

class AudioService(private val assets: Assets,private var soundVolume : Float = 1f , private var musicVolume : Float = 1f)
    : GameEventListener{

    private val soundCache = mutableMapOf<SoundAsset,Sound>()
    private var currentMusicResource : MusicResource? = null

    companion object{
        var mute : Boolean = false
    }
    init {
        GameEventDispatcher.register(this)
        SoundAsset.entries.forEach {
            soundCache[it] = assets[it]
        }
    }

    fun play(soundAsset: SoundAsset,loop : Boolean = false){
        val sound = soundCache[soundAsset]!!
        sound.stop()
        if (!loop) sound.play(soundVolume) else sound.loop(soundVolume)
    }

    fun play(musicAsset: MusicAsset){
        if (mute) return
        if (currentMusicResource?.musicAsset == musicAsset && currentMusicResource!!.music.isPlaying){
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
            isLooping = true
        }
    }

    private fun stopMusic(){
        if (currentMusicResource == null){
            return
        }
        currentMusicResource!!.music.stop()
    }

    private fun stopSounds(){
        soundCache.forEach { (soundAsset, sound) ->
            if (soundAsset != SoundAsset.FLAG){
                sound.stop()
            }
        }
    }

    fun stopSound(soundAsset: SoundAsset){
        soundCache[soundAsset]?.stop()
    }


    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent ->{
                stopSounds()
                gameEvent.tiledMap.propertyOrNull<String>("musicAsset")?.let {
                    play(MusicAsset.valueOf(it))
                }
            }
            is VictoryEvent ->{
                stopMusic()
                stopSounds()
                play(gameEvent.soundAsset)
            }
            is MainMenuEvent ->{
                stopSounds()
                play(MusicAsset.MUSIC6)
            }
            is CollectItemEvent -> {
                play(SoundAsset.COLLECT)
            }
            is PlaySoundEvent ->{
                play(gameEvent.soundAsset)
            }
            is MuteAudioEvent ->{
                val music = currentMusicResource?.music?:return
               if (gameEvent.mute) {
                    mute = true
                    music.volume =  0f
                    music.pause()
               } else {
                    mute = false
                    music.volume = this.musicVolume
                    music.play()
               }
            }
            is EntityLifeChangeEvent -> {
                if (gameEvent.currentLife == 4) return
                if (gameEvent.currentLife == 0) {
                    play(SoundAsset.DEATH)
                    return
                }
                play(SoundAsset.HURT)
            }
            else -> Unit
        }
    }
}
