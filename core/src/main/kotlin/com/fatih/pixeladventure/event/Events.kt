package com.fatih.pixeladventure.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Fixture
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity

sealed interface GameEvent

data class MapChangeEvent(val tiledMap: TiledMap) : GameEvent
data class EntityLifeChangeEvent(val currentLife : Int) : GameEvent
data class VictoryEvent(val soundAsset: SoundAsset) : GameEvent
data class CollectItemEvent(val playerEntity : Entity,val collectEntity : Entity) : GameEvent
data object RestartGameEvent : GameEvent
data class PlayerOutOfMapEvent(val playerEntity : Entity) : GameEvent
data object MainMenuEvent : GameEvent
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
