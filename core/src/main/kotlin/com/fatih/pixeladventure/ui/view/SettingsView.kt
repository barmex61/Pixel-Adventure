package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.MainMenuEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.screen.GameScreen
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.checkBox
import ktx.scene2d.defaultStyle
import ktx.scene2d.image
import ktx.scene2d.table
import ktx.scene2d.textButton


class SettingsView(
    skin : Skin,
    game : PixelAdventure
) : KTable , Table(skin){

    init {
        align(Align.top)
        setFillParent(true)
        table {tableCell ->
            background = skin.getDrawable("frame")
            tableCell.expand()
            val typingLabel = TypingLabel("{RAINBOW}{WAVE}Settings",skin, defaultStyle).apply{
                setAlignment(Align.center)
                color = skin.getColor("white")
            }
            add(typingLabel).padLeft(30.0f).padRight(30.0f).padTop(20.0f).padBottom(10.0f).prefWidth(120.0f).prefHeight(35.0f).colspan(2)

            row()
            textButton("Resume","menu_txt_button"){
                it.padBottom(10.0f).prefWidth(80.0f).colspan(2)
                onClick {
                    game.getScreen<GameScreen>().stopGame(false)
                }
            }
            row()
            textButton("Main Menu","menu_txt_button"){
                it.padBottom(10.0f).prefWidth(80.0f).colspan(2)
                onClick {
                    GameEventDispatcher.fireEvent(MainMenuEvent)
                }
            }
            row()
            textButton("Exit","menu_txt_button"){
                it.padBottom(10.0f).prefWidth(80.0f).colspan(2)
                onClick {
                    Gdx.app.exit()
                }
            }
            row()
            image("Volume"){
                it.padRight(10.0f).padBottom(10.0f).expand().align(Align.right)
            }
            checkBox(""){
                it.padBottom(10.0f).expand().align(Align.left).minSize(10.0f).prefSize(30.0f)
            }

        }
    }
}

@Scene2dDsl
fun <S>KWidget<S>.settingsView(
    skin: Skin = Scene2DSkin.defaultSkin,
    game : PixelAdventure ,
    init : SettingsView.(S) -> Unit = {}
) : SettingsView = actor(SettingsView(skin,game),init)
