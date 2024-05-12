package com.fatih.pixeladventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.system.AnimationSystem
import com.fatih.pixeladventure.ecs.system.CameraSystem
import com.fatih.pixeladventure.ecs.system.DamageSystem
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.ecs.system.GlProfilerSystem
import com.fatih.pixeladventure.ecs.system.InvulnarableSystem
import com.fatih.pixeladventure.ecs.system.JumpSystem
import com.fatih.pixeladventure.ecs.system.MoveSystem
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem
import com.fatih.pixeladventure.ecs.system.PhysicSystem
import com.fatih.pixeladventure.ecs.system.RenderSystem
import com.fatih.pixeladventure.ecs.system.StateSystem
import com.fatih.pixeladventure.ecs.system.TrackSystem
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.input.KeyboardInputProcessor
import com.fatih.pixeladventure.tiled.TiledService
import com.fatih.pixeladventure.ui.model.GameModel
import com.fatih.pixeladventure.ui.view.gameView
import com.fatih.pixeladventure.util.GameProperties
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.math.vec2
import ktx.scene2d.actors

class GameScreen(
    spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val assets: Assets,
    audioService: AudioService,
    gameProperties: GameProperties
): KtxScreen {

    private val gameViewPort : Viewport = StretchViewport(16f,9f)
    private val uiViewPort : Viewport = StretchViewport(320f,180f)
    private val uiStage : Stage = Stage(uiViewPort,spriteBatch)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
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
            add(MoveSystem())
            add(TrackSystem())
            add(JumpSystem())
            add(PhysicSystem())
            add(DamageSystem())
            add(InvulnarableSystem())
            add(StateSystem())
            add(AnimationSystem())
            add(CameraSystem())
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
    private val keyboardInputProcessor = KeyboardInputProcessor(world)

    override fun show() {
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        inputMultiplexer.addProcessor(uiStage)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        GameEventDispatcher.register(gameModel)
        GameEventDispatcher.register(tiledService)
        uiStage.actors {
            gameView(gameModel)
        }
        val map = assets[MapAsset.TEST]
        GameEventDispatcher.fireEvent(MapChangeEvent(map))

    }

    override fun hide() {
        inputMultiplexer.clear()
        GameEventDispatcher.unregister(gameModel)
        GameEventDispatcher.unregister(tiledService)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.unregister(it) }
        uiStage.clear()
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
        uiViewPort.update(width,height,true)
    }

    override fun dispose() {
        world.dispose()
        physicWorld.disposeSafely()
        uiStage.disposeSafely()
    }
}
