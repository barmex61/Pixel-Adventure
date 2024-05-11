package com.fatih.pixeladventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.system.AnimationSystem
import com.fatih.pixeladventure.ecs.system.CameraSystem
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.util.MapAsset
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.ecs.system.GlProfilerSystem
import com.fatih.pixeladventure.ecs.system.JumpSystem
import com.fatih.pixeladventure.ecs.system.MoveSystem
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem
import com.fatih.pixeladventure.ecs.system.PhysicSystem
import com.fatih.pixeladventure.ecs.system.RenderSystem
import com.fatih.pixeladventure.ecs.system.SpawnSystem
import com.fatih.pixeladventure.ecs.system.StateSystem
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.inputMultiplexer
import com.fatih.pixeladventure.input.KeyboardInputProcessor
import com.fatih.pixeladventure.util.GameProperties
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.math.vec2

class GameScreen(
    spriteBatch: SpriteBatch,
    private val physicWorld: PhysicWorld,
    private val assets: Assets,
    audioService: AudioService,
    gameProperties: GameProperties
): KtxScreen {

    private val gameViewPort : ExtendViewport = ExtendViewport(16f,9f)
    private val gameCamera = gameViewPort.camera as OrthographicCamera
    private val world = configureWorld {
        injectables {
            add("gameViewport",gameViewPort)
            add(gameCamera)
            add(assets)
            add(spriteBatch)
            add(physicWorld)
            add(audioService)
        }
        systems {
            add(SpawnSystem())
            add(MoveSystem())
            add(JumpSystem())
            add(PhysicSystem())
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
    private val keyboardInputProcessor = KeyboardInputProcessor(world)

    override fun show() {
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.register(it) }
        val map = assets[MapAsset.TEST]
        GameEventDispatcher.fireEvent(MapChangeEvent(map))
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(keyboardInputProcessor)
        world.systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.unregister(it) }
    }

    override fun render(delta: Float) {
        world.update(delta)
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            world.family { all(EntityTag.PLAYER) }.forEach { entity ->
                val body = entity[Physic].body
                entity[Physic].body.applyLinearImpulse(vec2(-4f,0f),body.worldCenter,true)
            }
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            world.family { all(EntityTag.PLAYER) }.forEach { entity ->
                val body = entity[Physic].body
                entity[Physic].body.applyLinearImpulse(vec2(4f,0f),body.worldCenter,true)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width,height,true)
    }

    override fun dispose() {
        world.dispose()
        physicWorld.disposeSafely()
    }
}
