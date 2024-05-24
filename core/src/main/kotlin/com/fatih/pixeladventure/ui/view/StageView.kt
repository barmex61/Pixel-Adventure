package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
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

class StageView(
    skin : Skin,
    menuModel: MenuModel
): Table(skin), KTable {

    init {
        setFillParent(true)
        val typingLabel = TypingLabel("{RAINBOW}{JUMP}Stages",skin, defaultStyle).apply{
            setAlignment(Align.center)
        }
        add(typingLabel).padTop(20.0f).minSize(20.0f).prefWidth(150.0f).prefHeight(40.0f).colspan(3)
        row()
        imageButton("back_img_button"){
            it.expand().align(Align.top)
            onClick {
                this@StageView.touchable = Touchable.disabled
                this@StageView += Actions.fadeOut(0.75f)
                menuModel.addActionToView(Actions.fadeIn(0.75f),MenuScreen.MenuViewType.MENU_VIEW)
                GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
            }
        }
        table { tableCell ->
            tableCell.expand()
            textButton("Tutorial","menu_txt_button"){
                it.padBottom(15.0f).minSize(20.0f).prefWidth(100.0f).prefHeight(40.0f)
                onClick {
                    menuModel.startGame(MapAsset.MAP15)
                    GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
                }
            }
            row()
            textButton("Stage 1","menu_txt_button"){
                it.padBottom(15.0f).minSize(20.0f).prefWidth(100.0f).prefHeight(40.0f)
                val isUnlocked = menuModel.isUnlocked(MapAsset.MAP1)
                isDisabled = !isUnlocked
                touchable = if (isUnlocked) Touchable.enabled else Touchable.disabled
                onClick {
                    this@StageView.touchable = Touchable.disabled
                    this@StageView += Actions.fadeOut(0.75f)
                    menuModel.addActionToView(Actions.fadeIn(0.75f),MenuScreen.MenuViewType.LEVEL_VIEW)
                    GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
                }
            }
            row()
            textButton("Stage 2","menu_txt_button"){
                it.minSize(20.0f).prefWidth(100.0f).prefHeight(40.0f)
                isDisabled = true
                onClick {
                    GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
                }
            }
        }
        add().expandX().expandY().prefSize(15f)

    }

}


@Scene2dDsl
fun <S> KWidget<S>.stageView(
    skin: Skin = Scene2DSkin.defaultSkin,
    menuModel: MenuModel,
    init : StageView.(S) -> Unit = {},
) : StageView = actor(StageView(skin,menuModel),init)


