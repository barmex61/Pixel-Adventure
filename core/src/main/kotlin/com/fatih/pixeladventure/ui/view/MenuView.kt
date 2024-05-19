package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.ui.model.MenuModel
import com.fatih.pixeladventure.util.MapAsset
import ktx.actors.onClick
import ktx.app.KtxScreen
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.textButton

class MenuView (
    skin: Skin,
    menuModel: MenuModel
): Table(skin), KTable {


    init {
        padTop(15f).padBottom(15f)
        setFillParent(true)
        menuTextButton(MapAsset.TUTORIAL,menuModel)
        menuTextButton(MapAsset.MAP1,menuModel)
        menuTextButton(MapAsset.MAP2,menuModel)
        menuTextButton(MapAsset.MAP3,menuModel)
        menuTextButton(MapAsset.MAP4,menuModel)
        menuTextButton(MapAsset.MAP5,menuModel)
        menuTextButton(MapAsset.MAP6,menuModel)

    }

    private fun menuTextButton(mapAsset: MapAsset,menuModel: MenuModel){
        textButton(mapAsset.mapName,"menu_txt_button",skin){
            it.expandY().fillX().prefWidth(150f)
            val isUnlocked = menuModel.isUnlocked(mapAsset)
            isDisabled = !isUnlocked
            touchable = if (isUnlocked) Touchable.enabled else Touchable.disabled
            onClick { menuModel.startGame(mapAsset) }
        }
        row()
    }
}


@Scene2dDsl
fun <S> KWidget<S>.menuView(
    skin: Skin = Scene2DSkin.defaultSkin,
    menuModel: MenuModel ,
    init : MenuView.(S) -> Unit = {}
) : MenuView = actor(MenuView(skin,menuModel),init)

