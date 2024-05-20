package com.fatih.pixeladventure.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.system.AnimationSystem
import com.fatih.pixeladventure.ecs.system.BlinkSystem
import com.fatih.pixeladventure.ecs.system.CameraSystem
import com.fatih.pixeladventure.ecs.system.FlashSystem
import com.fatih.pixeladventure.ecs.system.FlySystem
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.ecs.system.GlProfilerSystem
import com.fatih.pixeladventure.ecs.system.InvulnarableSystem
import com.fatih.pixeladventure.ecs.system.JumpSystem
import com.fatih.pixeladventure.ecs.system.DamageSystem
import com.fatih.pixeladventure.ecs.system.MoveSystem
import com.fatih.pixeladventure.ecs.system.ParallaxBgdSystem
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem
import com.fatih.pixeladventure.ecs.system.PhysicSystem
import com.fatih.pixeladventure.ecs.system.RenderSystem
import com.fatih.pixeladventure.ecs.system.StateSystem
import com.fatih.pixeladventure.ecs.system.TeleportSystem
import com.fatih.pixeladventure.ecs.system.TextSystem
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.VictoryEvent
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.input.KeyboardInputProcessor
import com.fatih.pixeladventure.tiled.TiledService
import com.fatih.pixeladventure.ui.model.GameModel
import com.fatih.pixeladventure.ui.view.gameView
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.GameProperties
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.collections.GdxArray
import ktx.scene2d.actors

class GameScreen(
    spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val assets: Assets,
    audioService: AudioService,
    gameProperties: GameProperties,
    private val game : PixelAdventure,
    private val gamePreferences: GamePreferences,
): KtxScreen , GameEventListener{

    private var stopGame : Boolean = false
    private val gameViewPort : Viewport = StretchViewport(18f,11f)
    private val uiViewPort : Viewport = StretchViewport(480f,270f)
    private val uiStage : Stage = Stage(uiViewPort,spriteBatch)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
    private var delayToMenu = 0f
    private var isPlayerDeath = false
    private lateinit var currentMapAsset: MapAsset
    private val world = configureWorld {
        injectables {
            add("gameViewport",gameViewPort)
            add("uiViewport",uiViewPort)
            add(uiStage)
            add(gameCamera)
            add(assets)
            add(spriteBatch)
            add(physicWorld)
            add(audioService)
        }
        systems {
            add(DamageSystem())
            add(AnimationSystem())
            add(MoveSystem())
            add(JumpSystem())
            add(PhysicSystem())
            add(TeleportSystem())
            add(InvulnarableSystem())
            add(FlySystem())
            add(StateSystem())
            add(CameraSystem())
            add(BlinkSystem())
            add(FlashSystem())
            add(ParallaxBgdSystem())
            add(TextSystem())
            add(RenderSystem())

            if (gameProperties.debugPhysic){
                add(PhysicDebugRenderSystem())
            }
            if (gameProperties.enableProfiling){
                add(GlProfilerSystem())
            }
        }
    }
    private val tiledService = TiledService(physicWorld,assets,world)
    private val gameModel : GameModel = GameModel(world)
    private val keyboardInputProcessor: KeyboardInputProcessor = KeyboardInputProcessor(world)

    fun loadMap(mapAsset: MapAsset){
        currentMapAsset = mapAsset
        val map = assets[mapAsset]
        world.system<ParallaxBgdSystem>().parallaxBgd.setTexture(mapAsset.parallaxBgdTexture)
        GameEventDispatcher.fireEvent(MapChangeEvent(map))
    }

    override fun show() {
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        inputMultiplexer.addProcessor(uiStage)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        GameEventDispatcher.register(gameModel)
        GameEventDispatcher.register(tiledService)
        GameEventDispatcher.register(this)
        uiStage.actors {
            gameView(gameModel, game = game)
        }

    }

    override fun hide() {
        keyboardInputProcessor.resetMoveX()
        inputMultiplexer.clear()
        GameEventDispatcher.unregister(gameModel)
        GameEventDispatcher.unregister(tiledService)
        GameEventDispatcher.unregister(this)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.unregister(it) }
        uiStage.clear()
    }

    override fun render(delta: Float) {
        world.update(if (stopGame) 0f else delta)
        if (delayToMenu > 0f){
            delayToMenu -= delta
            if (delayToMenu < 0f){
                onFinishMap()
            }
        }
    }

    fun stopGame(){
        stopGame = !stopGame
        keyboardInputProcessor.stop = !keyboardInputProcessor.stop
        keyboardInputProcessor.resetMoveX()
    }

    private fun onFinishMap(){
        delayToMenu = 0f
        if (!isPlayerDeath) currentMapAsset.unlocksMap?.let { mapAsset -> gamePreferences.storeUnlockedMap(mapAsset) }
        isPlayerDeath = false
        world.removeAll()
        val bodyList = GdxArray<Body>()
        physicWorld.getBodies(bodyList)
        bodyList.forEach { physicWorld.destroyBody(it) }
        game.setScreen<MenuScreen>()
        game.getScreen<MenuScreen>().addAction(fadeIn(0.75f),MenuScreen.ViewType.LEVEL_VIEW)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
        uiViewPort.update(width,height,true)
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is VictoryEvent ->{
                delayToMenu = 2f
            }
            is EntityLifeChangeEvent ->{
                if (gameEvent.currentLife == 0){
                    delayToMenu = 0.00001f
                    isPlayerDeath = true
                }
            }
            else -> Unit
        }
    }

    override fun dispose() {
        world.dispose()
        physicWorld.disposeSafely()
        uiStage.disposeSafely()
    }
}
