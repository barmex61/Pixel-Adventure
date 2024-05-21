package com.fatih.pixeladventure.ui.model


import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.event.RestartLevelEvent
import com.fatih.pixeladventure.ui.PropertyChangeSource
import com.fatih.pixeladventure.ui.propertyNotify
import com.github.quillraven.fleks.World
import ktx.tiled.property

class GameModel(val world: World) : GameEventListener , PropertyChangeSource(){

    var playerLife by propertyNotify(0)
    var mapName by propertyNotify("")


    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent ->{
                mapName = gameEvent.tiledMap.property("name","MISS_MAP_NAME")
            }
            is EntityLifeChangeEvent -> {
                playerLife = gameEvent.currentLife
            }
            is RestartLevelEvent -> {
                playerLife = 4
            }
            else -> Unit
        }
    }

}
