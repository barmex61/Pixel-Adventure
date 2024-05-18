package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.ui.model.GameModel
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.image

class GameView(
    gameModel : GameModel,
    skin: Skin
) : Table(skin),KTable{

    init {
        setFillParent(true)
       val typingLabel = TypingLabel("",skin, defaultStyle).apply{
           setAlignment(Align.center)
           color = skin.getColor("white")
           this.color.a = 0f
        }
        this.add(typingLabel).padTop(10f).expand().align(Align.top).prefWidth(180f).prefHeight(40f)
        row()
        val playerLife = image("health_4"){
            name = "player_life"
            it.padLeft(7f).padBottom(10f).expand().align(Align.bottomLeft).minWidth(70f).minHeight(10f)
        }

        gameModel.onPropertyChange(GameModel::mapName){
            typingLabel.clearActions()
            typingLabel += Actions.sequence(
                fadeIn(2f),
                delay(1f),
                fadeOut(1f)
            )
            typingLabel.setText("{RAINBOW}{HANG}$it")
        }
        gameModel.onPropertyChange(GameModel::playerLife){
            playerLife.drawable = skin.getDrawable("health_$it")
        }
    }

}

@Scene2dDsl
fun <S>KWidget<S>.gameView(
    gameModel: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init : GameView.(S) -> Unit = {}
) : GameView = actor(GameView(gameModel,skin),init)
