package com.fatih.pixeladventure.event

import com.badlogic.gdx.maps.tiled.TiledMap

sealed interface GameEvent

data class MapChangeEvent(val tiledMap: TiledMap) : GameEvent

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

    fun GameEventListener.fireEvent(gameEvent: GameEvent){
        this.onEvent(gameEvent)
    }
}
