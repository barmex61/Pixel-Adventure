package com.fatih.pixeladventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.ai.FruitState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.Collectable
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Fly
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.State
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
import com.fatih.pixeladventure.event.CollectItemEvent
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.MainMenuEvent
import com.fatih.pixeladventure.event.PlayerOutOfMapEvent
import com.fatih.pixeladventure.event.EndFruitEffectEvent
import com.fatih.pixeladventure.event.RestartLevelEvent
import com.fatih.pixeladventure.event.VictoryEvent
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.input.KeyboardInputProcessor
import com.fatih.pixeladventure.tiled.TiledService
import com.fatih.pixeladventure.ui.model.GameModel
import com.fatih.pixeladventure.ui.view.GameView
import com.fatih.pixeladventure.ui.view.SettingsView
import com.fatih.pixeladventure.ui.view.gameView
import com.fatih.pixeladventure.ui.view.settingsView
import com.fatih.pixeladventure.util.FruitDrawable
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.GameProperties
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.collections.GdxArray
import ktx.scene2d.actors

class GameScreen(
    spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val assets: Assets,
    private val audioService: AudioService,
    gameProperties: GameProperties,
    private val game : PixelAdventure,
    private val gamePreferences: GamePreferences,
): KtxScreen , GameEventListener {


    private var settingsView: SettingsView ?= null
    private var gameView : GameView ?= null
    private var stopGame : Boolean = false
    private var menuViewType = MenuScreen.MenuViewType.STAGE_VIEW
    private val gameViewPort : Viewport = StretchViewport(21f,13f)
    private val uiViewPort : Viewport = StretchViewport(480f,270f)
    private val uiStage : Stage = Stage(uiViewPort,spriteBatch)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
    private var isPlayerDeath = false
    private var changeScreen : Boolean = false
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
            add(AnimationSystem())
            add(MoveSystem())
            add(JumpSystem())
            add(PhysicSystem())
            add(TeleportSystem())
            add(BlinkSystem())
            add(InvulnarableSystem())
            add(FlashSystem())
            add(DamageSystem())
            add(FlySystem())
            add(StateSystem())
            add(CameraSystem())
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
        keyboardInputProcessor.stopMovement = false
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        inputMultiplexer.addProcessor(uiStage)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        GameEventDispatcher.register(gameModel)
        GameEventDispatcher.register(tiledService)
        GameEventDispatcher.register(this)
        uiStage.actors {
            gameView = gameView(gameModel, game = game, keyboardInputProcessor = keyboardInputProcessor)
            settingsView = settingsView(game = game){
                isVisible = false
            }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            gameView?.addFruit(FruitDrawable.entries.random())
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
            gameView?.deleteFruit(FruitDrawable.entries.random())
        }
        world.update(if (stopGame) 0f else delta)
        if (changeScreen){
            gameCamera.zoom -= delta
            if (gameCamera.zoom <= 0f) {
                changeScreen = false
                onFinishMap()
            }
        }
        if (stopGame) {
            gameView?.act(delta)
            settingsView?.act(delta)
        }
    }

    fun stopGame(stop : Boolean){
        stopGame = stop
        keyboardInputProcessor.resetMoveX()
        audioService.play(SoundAsset.PAUSE)
        settingsView?.isVisible = stop
        gameView?.touchable = if (stop) Touchable.disabled else Touchable.enabled
    }


    private fun onFinishMap(){
        changeScreen = false
        if (!isPlayerDeath) currentMapAsset.unlocksMap?.let { mapAsset -> gamePreferences.storeUnlockedMap(mapAsset) }
        isPlayerDeath = false
        world.removeAll()
        val bodyList = GdxArray<Body>()
        physicWorld.getBodies(bodyList)
        bodyList.forEach { physicWorld.destroyBody(it) }
        game.setScreen<MenuScreen>()
        game.getScreen<MenuScreen>().addAction(fadeIn(0.75f),menuViewType)
        gameCamera.zoom = 1f
    }

    private fun restartLevel(){
        world.removeAll()
        val bodyList = GdxArray<Body>()
        physicWorld.getBodies(bodyList)
        bodyList.forEach { physicWorld.destroyBody(it) }
        loadMap(currentMapAsset)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
        uiViewPort.update(width,height,true)
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is VictoryEvent ->{
                keyboardInputProcessor.stopMovement = true
                menuViewType = MenuScreen.MenuViewType.LEVEL_VIEW
                changeScreen = true
                gameView?.touchable = Touchable.disabled
            }
            is EntityLifeChangeEvent ->{
                menuViewType = MenuScreen.MenuViewType.LEVEL_VIEW
                if (gameEvent.currentLife == 0){
                    keyboardInputProcessor.stopMovement = true
                    changeScreen = true
                    isPlayerDeath = true
                }
            }
            is MainMenuEvent ->{
                keyboardInputProcessor.stopMovement = true
                menuViewType = MenuScreen.MenuViewType.MENU_VIEW
                stopGame(false)
                changeScreen = true
                isPlayerDeath = true
                gameView?.touchable = Touchable.disabled
            }
            is RestartLevelEvent ->{
                restartLevel()
                gameView?.clearFruitTable()
            }

            is CollectItemEvent->{
                with(world){
                    val collectableEntity = gameEvent.collectEntity
                    collectableEntity.configure {
                        it -= EntityTag.COLLECTABLE
                    }
                    collectableEntity[State].stateMachine.changeState(FruitState.HIT_RESPAWN)
                    when(collectableEntity[Collectable].name){
                        GameObject.CHERRY.name -> {
                            gameEvent.playerEntity[Jump].doubleJump = true
                            //gameView?.addFruit(FruitDrawable.CHERRY)
                        }
                        GameObject.BANANA.name -> {
                            gameEvent.playerEntity[Move].max += 1f
                            gameView?.addFruit(FruitDrawable.BANANA)
                        }
                        GameObject.MELON.name -> {
                            gameEvent.playerEntity[Jump].wallJumpFruitTimer = 2.5f
                            gameView?.addFruit(FruitDrawable.MELON)
                        }
                        GameObject.PINEAPPLE.name -> {
                            gameEvent.playerEntity.configure {
                                it += Fly(2f)
                            }
                            gameView?.addFruit(FruitDrawable.PINEAPPLE)
                        }
                        GameObject.KIWI.name ->{
                            gameEvent.playerEntity.configure {
                                it += Invulnarable(3.5f,true)
                                it += Blink(3.3f,0.075f)
                            }
                            gameView?.addFruit(FruitDrawable.KIWI)
                        }
                        GameObject.APPLE.name -> {
                            gameEvent.playerEntity[Jump].doubleJumpFruitTimer = 4f
                            gameView?.addFruit(FruitDrawable.APPLE)
                        }

                        else -> Unit
                    }
                }
            }
            is EndFruitEffectEvent ->{
                gameView?.deleteFruit(gameEvent.fruitDrawable,true,gameEvent.count)
            }

            is PlayerOutOfMapEvent ->{
                gameView?.deleteFruit(FruitDrawable.BANANA,true,0)
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

