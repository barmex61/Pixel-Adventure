package com.fatih.pixeladventure.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.screen.LoadingScreen
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.earthGravity

typealias PhysicWorld = World

val inputMultiplexer : InputMultiplexer
    get() = Gdx.input.inputProcessor as InputMultiplexer

class PixelAdventure : KtxGame<KtxScreen>() {

    private val spriteBatch : SpriteBatch by lazy { SpriteBatch() }
    private val assets : Assets by lazy { Assets() }
    private val physicWorld : PhysicWorld = World(earthGravity,true).apply { autoClearForces = false }

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer()
        addScreen(GameScreen(spriteBatch,physicWorld,assets))
        addScreen(LoadingScreen(this,assets))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        spriteBatch.disposeSafely()
        assets.dispose()
    }

    companion object{
        const val UNIT_SCALE = 1/16f
        val OBJECT_FIXTURES = mutableMapOf<GameObject,List<FixtureDef>>()
    }
}
