package com.fatih.pixeladventure.ui.model

import com.badlogic.gdx.scenes.scene2d.Action
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.screen.MenuScreen
import com.fatih.pixeladventure.screen.MenuScreen.*
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.MapAsset

class MenuModel(val game : PixelAdventure,val preferences: GamePreferences){

    private val unlockedMaps : Set<MapAsset>
        get() = preferences.loadUnlockedMaps()

    fun startGame(mapAsset: MapAsset){
        game.getScreen<MenuScreen>().mapAsset  = mapAsset

    }

    fun addActionToView(actions : Action, menuViewType: MenuViewType){
        game.getScreen<MenuScreen>().addAction(actions,menuViewType)
    }

    fun isUnlocked(mapAsset: MapAsset) = unlockedMaps.contains(mapAsset)

}
