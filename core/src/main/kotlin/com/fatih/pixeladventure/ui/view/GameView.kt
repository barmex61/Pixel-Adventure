package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.PlaySoundEvent
import com.fatih.pixeladventure.event.RestartLevelEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.ui.model.GameModel
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
import ktx.scene2d.image
import ktx.scene2d.imageButton

class GameView(
    gameModel : GameModel,
    skin: Skin,
    game : PixelAdventure
) : Table(skin),KTable{

    val typingLabelCell : Cell<TypingLabel>


    init {
        align(Align.top)
        setFillParent(true)
        imageButton("restart_img_button"){
            it.padLeft(15f).padTop(15f).align(Align.top).prefSize(17f)
            onClick {
                GameEventDispatcher.fireEvent(RestartLevelEvent)
                GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
            }
        }
        val typingLabel = TypingLabel("",skin, defaultStyle).apply{
           setAlignment(Align.center)
           color = skin.getColor("white")
        }
        typingLabelCell = this.add(typingLabel)
        typingLabelCell.padTop(15f).expand().align(Align.top).prefHeight(45f)
        imageButton("setting_img_button"){
            it.padRight(15f).padTop(15f).align(Align.top).prefSize(17f)
            onClick {
                game.getScreen<GameScreen>().stopGame(true)
            }
        }
        row()
        val playerLife = image("health_4"){
            name = "player_life"
            it.padLeft(15f).padBottom(15f).expandY().align(Align.bottomLeft).prefWidth(90f).prefHeight(13f).colspan(2)
        }

        gameModel.onPropertyChange(GameModel::mapName){
            typingLabel.clearActions()
            val mapNameLength = it.length
            typingLabelCell.prefWidth(mapNameLength * 15f)
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
    game : PixelAdventure,
    init : GameView.(S) -> Unit = {}
) : GameView = actor(GameView(gameModel,skin,game),init)
