package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.PlaySoundEvent
import com.fatih.pixeladventure.screen.MenuScreen
import com.fatih.pixeladventure.ui.model.MenuModel
import com.fatih.pixeladventure.util.SoundAsset
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.table
import ktx.scene2d.textButton

class MenuView(
    skin: Skin,
    menuModel: MenuModel
) : Table(skin), KTable {


    init {
        setFillParent(true)
        val typingLabel = TypingLabel("{RAINBOW}{HANG}Pixel Adventure",skin, defaultStyle).apply{
            setAlignment(Align.center)
        }
        this.add(typingLabel).prefWidth(200f).prefHeight(40f)
        row()
        table {
            this.pad(25f)
            textButton("Play","menu_txt_button"){
                it.padBottom(15f).prefWidth(100f)
                onClick {
                    this@MenuView.touchable = Touchable.disabled
                    this@MenuView += fadeOut(0.75f)
                    menuModel.addActionToView(fadeIn(0.75f),MenuScreen.MenuViewType.STAGE_VIEW)
                    GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
                }
            }
            row()
            textButton("Exit","menu_txt_button"){
                it.prefWidth(100f)
                onClick {
                    Gdx.app.exit()
                }
            }
        }

    }
}


@Scene2dDsl
fun <S> KWidget<S>.menuView(
    skin: Skin = Scene2DSkin.defaultSkin,
    menuModel: MenuModel,
    init : MenuView.(S) -> Unit = {},
) : MenuView = actor(MenuView(skin,menuModel),init)
