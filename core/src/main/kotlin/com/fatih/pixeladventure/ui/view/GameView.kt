package com.fatih.pixeladventure.ui.view

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.utils.Align
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.PlaySoundEvent
import com.fatih.pixeladventure.event.RestartLevelEvent
import com.fatih.pixeladventure.event.TouchpadAlphaEvent
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.input.KeyboardInputProcessor
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.ui.model.GameModel
import com.fatih.pixeladventure.util.FruitDrawable
import com.fatih.pixeladventure.util.SoundAsset
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.KtxInputListener
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.onKeyDown
import ktx.actors.onTouchDown
import ktx.actors.onTouchUp
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.defaultStyle
import ktx.scene2d.image
import ktx.scene2d.imageButton
import ktx.scene2d.table
import ktx.scene2d.textField
import ktx.scene2d.touchpad

class GameView(
    gameModel : GameModel,
    skin: Skin,
    game : PixelAdventure,
    keyboardInputProcessor: KeyboardInputProcessor
) : Table(skin),KTable{

    private val typingLabelCell : Cell<TypingLabel>
    private val fruitTable : Table
    private val blankDrawable = skin.getDrawable("blank")
    private var touchpad : Touchpad? = null

    init {
        setFillParent(true)
        imageButton("restart_img_button"){
            it.padLeft(20.0f).align(Align.left)
            onClick {
                GameEventDispatcher.fireEvent(RestartLevelEvent)
                GameEventDispatcher.fireEvent(PlaySoundEvent(SoundAsset.PAUSE))
            }
        }
        val typingLabel = TypingLabel("",skin, defaultStyle).apply{
           setAlignment(Align.center)
        }
        typingLabelCell = this.add(typingLabel)
        typingLabelCell.padTop(15.0f).expandX().minWidth(150.0f).minHeight(40.0f)
        imageButton("setting_img_button"){
            it.padRight(20.0f).align(Align.right)
            onClick {
                game.getScreen<GameScreen>().stopGame(true)
            }
        }
        row()
        fruitTable = table {fruitTableCell->
            align(Align.topLeft)
            fruitTableCell.grow().colspan(3).padLeft(10f)
            repeat((0..5).count()) {
                image("blank") {
                    it.minSize(5f).prefSize(25f)
                }
                textField {
                    it.minSize(5f).prefSize(25f).align(Align.left).padBottom(1f)
                }
                row()
            }

        }
        if (Gdx.app.type == Application.ApplicationType.Android || Gdx.app.type == Application.ApplicationType.iOS ){
            row()
            this.touchpad = touchpad(0f){
                this.alpha = 0.2f
                it.expandX().maxSize(64f).align(Align.left).padLeft(35f)
                listeners.add(object  : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        GameEventDispatcher.fireEvent(TouchpadAlphaEvent(1f))
                        return true
                    }

                    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                        GameEventDispatcher.fireEvent(TouchpadAlphaEvent(0.2f))
                        keyboardInputProcessor.resetMoveX()
                    }

                    override fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
                        if (x < 20f){
                            keyboardInputProcessor.updatePlayerMovement(-1)
                        }else if (x > 44f){
                            keyboardInputProcessor.updatePlayerMovement(1)
                        }else{
                            keyboardInputProcessor.resetMoveX()
                        }
                    }
                })
            }
            add().expandX()
            imageButton("jump_img_button"){
                it.expandX().maxSize(52f).align(Align.right).padRight(35f)
                listeners.add(object  : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        keyboardInputProcessor.updatePlayerJump(true)
                        return true
                    }

                    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                        keyboardInputProcessor.updatePlayerJump(false)
                    }
                })
            }
        }
        row()
        val playerLife = image("health_4"){
            name = "player_life"
            it.padLeft(20.0f).padBottom(20.0f).align(Align.left).colspan(3).prefSize(75f,if (Gdx.app.type == Application.ApplicationType.Android || Gdx.app.type == Application.ApplicationType.iOS) 25f else 12f)
            if (Gdx.app.type == Application.ApplicationType.iOS || Gdx.app.type == Application.ApplicationType.Android) {
                it.align(Align.center).padLeft(0f)
            }
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
        gameModel.onPropertyChange(GameModel::touchPadAlpha){
            touchpad?.alpha = it
        }
    }

    fun addFruit(fruitDrawable : FruitDrawable){

        fruitTable.cells.firstOrNull {
            (it.actor is Image) && ((it.actor as Image).drawable == skin.getDrawable(fruitDrawable.drawablePath))
        }?.let {
            setFruitCount(it,1)
            val fruImage = (it.actor as Image)
            if (fruImage.hasActions() && fruImage.name == "remove"){
                val (fruitImage,fruitTextField) = getFruitCells(it)
                addActionToFruit(fruitImage,fruitTextField,fruitDrawable)
                fruImage.name = ""
            }
            return
        }

        val fruitImageCell = fruitTable.cells.firstOrNull {
            (it.actor is Image) && ((it.actor as Image).drawable == this.blankDrawable)
        }?:return

        val (fruitImage,fruitTextField) = getFruitCells(fruitImageCell)
        addActionToFruit(fruitImage,fruitTextField,fruitDrawable)

    }

    fun deleteFruit(fruitDrawable: FruitDrawable,setCount : Boolean = false,count : Int = -1) {
        var fruitImageCell : Cell<Actor>? = null
         fruitTable.cells.firstOrNull { cell ->
            (cell.actor is Image) && (cell.actor as Image).drawable == skin.getDrawable(fruitDrawable.drawablePath)
        }?.let {
            fruitImageCell = it
            //fruit sayısı 0 ise devam et ve sil değilse azalt ve dön
            if (setFruitCount(it,count,setCount)) return
        }
        if (fruitImageCell == null) return

        val (fruitImage, fruitTextField) = getFruitCells(fruitImageCell!!)
        fruitImage.clearActions()
        fruitImage.name = "remove"
        fruitImage += Actions.sequence(fadeOut(1f), Actions.run { fruitImage.drawable = blankDrawable })

        fruitTextField.clearActions()
        fruitTextField += Actions.sequence(fadeOut(1f), Actions.run { fruitTextField.text = "" })
    }

    private fun addActionToFruit(fruitImage : Image,fruitTextField : TextField,fruitDrawable: FruitDrawable){
        fruitImage.alpha = 0f
        fruitImage.drawable = skin.getDrawable(fruitDrawable.drawablePath)
        fruitImage.clearActions()
        fruitImage += fadeIn(1f)

        fruitTextField.alpha = 0f
        fruitTextField.text = "x 1"
        fruitTextField.clearActions()
        fruitTextField += fadeIn(1f)
    }

    private fun getFruitCells(fruitImageCell: Cell<Actor>): Pair<Image,TextField> {
        val fruitImage = (fruitImageCell.actor as Image)
        val fruitTextCellIndex = fruitTable.cells.indexOf(fruitImageCell) + 1
        val fruitTextCell = fruitTable.cells.get(fruitTextCellIndex)
        val fruitTextField = (fruitTextCell.actor as TextField)
        return Pair(fruitImage, fruitTextField)
    }

    private fun setFruitCount(fruitImageCell: Cell<Actor>, value : Int,set : Boolean = false) : Boolean{
        if (value == 0) return false
        val fruitTextCellIndex = fruitTable.cells.indexOf(fruitImageCell) + 1
        val fruitTextCell = fruitTable.cells.get(fruitTextCellIndex)
        val fruitTextField = (fruitTextCell.actor as TextField)
        val fruitTextFieldFruitCount = fruitTextField.text.replace("x ","").toIntOrNull()?:return true
        val fruitCount = if (!set) fruitTextFieldFruitCount + value else value
        fruitTextField.text = "x ${fruitCount.coerceAtLeast(1)}"
        return fruitCount != 0
    }

}

@Scene2dDsl
fun <S>KWidget<S>.gameView(
    gameModel: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    game : PixelAdventure,
    keyboardInputProcessor: KeyboardInputProcessor,
    init : GameView.(S) -> Unit = {}
) : GameView = actor(GameView(gameModel,skin,game,keyboardInputProcessor),init)
