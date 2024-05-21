package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.PlaySoundEvent
import com.fatih.pixeladventure.screen.MenuScreen
import com.fatih.pixeladventure.ui.model.MenuModel
import com.fatih.pixeladventure.util.MapAsset
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
import ktx.scene2d.imageButton
import ktx.scene2d.table
import ktx.scene2d.textButton

class LevelView (
    skin: Skin,
    menuModel: MenuModel
): Table(skin), KTable {


    init {
        setFillParent(true)
        val typingLabel = TypingLabel("{RAINBOW}{JUMP}Levels",skin, defaultStyle).apply{
            setAlignment(Align.center)
        }
        add(typingLabel).expand().padTop(25f).minHeight(40f).minWidth(200f).colspan(3)
        row()
        imageButton("back_img_button"){
            it.padTop(17f).padRight(25f).expandX().align(Align.topRight)
            onClick {
                this@LevelView.touchable = Touchable.disabled
                this@LevelView += Actions.fadeOut(0.75f)
                menuModel.addActionToView(fadeIn(0.75f),MenuScreen.MenuViewType.STAGE_VIEW)
                GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
            }
        }
        table {
            it.expandX().padBottom(25f)
            (1..25).forEach {index ->
                val assetStr = "MAP$index"
                textButton("$index","menu_txt_button",skin){textButtonCell->
                    textButtonCell.minSize(35f).prefSize(35f).pad(5f)
                    val isUnlocked = menuModel.isUnlocked(MapAsset.valueOf(assetStr))
                    isDisabled = !isUnlocked
                    touchable = if (isUnlocked) Touchable.enabled else Touchable.disabled
                    onClick {
                        menuModel.startGame(MapAsset.valueOf(assetStr))
                        GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
                    }
                }
                if (index % 5 == 0){
                    row()
                }
            }
        }
        imageButton("next_img_button"){
            it.padTop(17f).padLeft(25f).expandX().align(Align.topLeft)
            isDisabled = true
            onClick {
                GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
            }
        }

    }

}


@Scene2dDsl
fun <S> KWidget<S>.levelView(
    skin: Skin = Scene2DSkin.defaultSkin,
    menuModel: MenuModel ,
    init : LevelView.(S) -> Unit = {},
) : LevelView = actor(LevelView(skin,menuModel),init)

