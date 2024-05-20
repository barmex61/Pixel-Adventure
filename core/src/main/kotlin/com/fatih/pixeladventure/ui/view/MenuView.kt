package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.ui.model.MenuModel
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.table
import ktx.scene2d.textButton

class MenuView(
    skin: Skin
) : Table(skin), KTable {


    init {
        setFillParent(true)
        val typingLabel = TypingLabel("{RAINBOW}{HANG}Pixel Adventure",skin, defaultStyle).apply{
            setAlignment(Align.center)
        }
        this.add(typingLabel).prefWidth(200f).prefHeight(40f)
        row()
        table {
            this.background = skin.getDrawable("frame")
            this.pad(25f)
            textButton("Play","menu_txt_button"){
                it.padBottom(15f).prefWidth(100f)
            }
            row()
            textButton("Settings","menu_txt_button"){
                it.padBottom(15f).prefWidth(100f)
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
    init : MenuView.(S) -> Unit = {}
) : MenuView = actor(MenuView(skin),init)
