package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.parallax.ParallaxBackground
import com.fatih.pixeladventure.ui.model.MenuModel
import com.fatih.pixeladventure.ui.view.LevelView
import com.fatih.pixeladventure.ui.view.MenuView
import com.fatih.pixeladventure.ui.view.StageView
import com.fatih.pixeladventure.ui.view.levelView
import com.fatih.pixeladventure.ui.view.menuView
import com.fatih.pixeladventure.ui.view.stageView
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.util.MusicAsset
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.math.vec2
import ktx.scene2d.actors

class MenuScreen(spriteBatch: SpriteBatch,private val audioService: AudioService,private val game : PixelAdventure,preferences: GamePreferences) : KtxScreen{

    enum class MenuViewType{
        MENU_VIEW,LEVEL_VIEW,STAGE_VIEW
    }

    private val viewPort = StretchViewport(480f,270f)
    private val camera : OrthographicCamera = viewPort.camera as OrthographicCamera
    private val menuStage = Stage(viewPort,spriteBatch)
    private val menuModel = MenuModel(game,preferences)
    private val parallaxBgd = ParallaxBackground(viewPort,"graphics/brown.png", vec2(1f,1f),1f)
    private lateinit var menuView: MenuView
    private lateinit var levelView: LevelView
    private lateinit var stageView: StageView
    private var firstLaunch = true
    var mapAsset : MapAsset? = null

    fun addAction(actions: Action, menuViewType: MenuViewType){
        when(menuViewType){
            MenuViewType.LEVEL_VIEW -> {
                levelView += actions
                levelView.touchable = Touchable.enabled
            }
            MenuViewType.STAGE_VIEW -> {
                stageView += actions
                stageView.touchable = Touchable.enabled
            }
            MenuViewType.MENU_VIEW -> {
                menuView += actions
                menuView.touchable = Touchable.enabled
            }
        }
    }

    override fun show() {
        inputMultiplexer.addProcessor(menuStage)
        menuStage.actors {
            menuView = menuView(menuModel = menuModel) {
                alpha = if (firstLaunch) 1f else 0f
                touchable = if (firstLaunch) Touchable.enabled else Touchable.disabled
            }
            stageView = stageView(menuModel = menuModel){
                alpha = 0f
                touchable = Touchable.disabled
            }
            levelView = levelView(menuModel = menuModel){
                alpha = 0f
                touchable = Touchable.disabled
            }
        }
        firstLaunch = false
        audioService.play(MusicAsset.MUSIC6)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(menuStage)
        menuStage.clear()
    }

    override fun render(delta: Float) {
        if (mapAsset != null){
            camera.zoom -= delta
            if (camera.zoom <= 0f){
                game.setScreen<GameScreen>()
                game.getScreen<GameScreen>().loadMap(mapAsset!!)
                mapAsset = null
                camera.zoom = 1f
            }
        }
        viewPort.apply()
        parallaxBgd.scrollBy(-0.5f * delta,-0.5f * delta)
        menuStage.batch.use(camera){
            parallaxBgd.draw(0f,0f,it)
        }
        menuStage.act(delta)
        menuStage.draw()

    }

    override fun resize(width: Int, height: Int) {
        menuStage.viewport.update(width,height)
    }

    override fun dispose() {
        inputMultiplexer.clear()
        menuStage.disposeSafely()
        parallaxBgd.disposeSafely()
    }
}
