package com.fatih.pixeladventure.event

import com.badlogic.gdx.Game
import com.badlogic.gdx.maps.tiled.TiledMap
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity

sealed interface GameEvent

data class MapChangeEvent(val tiledMap: TiledMap) : GameEvent
data class EntityLifeChangeEvent(val entity: Entity) : GameEvent
data class VictoryEvent(val soundAsset: SoundAsset) : GameEvent
data class CollectItemEvent(val soundAsset: SoundAsset) : GameEvent

interface GameEventListener{

    fun onEvent(gameEvent: GameEvent)

}

object GameEventDispatcher{

    private val gameEventListeners = mutableListOf<GameEventListener>()

    fun register(gameEventListener: GameEventListener){
        gameEventListeners += gameEventListener
    }

    fun unregister(gameEventListener: GameEventListener){
        gameEventListeners -= gameEventListener
    }

    fun fireEvent(gameEvent: GameEvent){
        gameEventListeners.forEach {
            it.onEvent(gameEvent)
        }
    }
}
