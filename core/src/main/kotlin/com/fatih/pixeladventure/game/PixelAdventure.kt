package com.fatih.pixeladventure.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.PropertiesUtils
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.screen.GameScreen
import com.fatih.pixeladventure.screen.LoadingScreen
import com.fatih.pixeladventure.tiled.TiledService.Companion.FixtureDefUserData
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.GamePreferences
import com.fatih.pixeladventure.util.GameProperties
import com.fatih.pixeladventure.util.toGameProperties
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.math.vec2

typealias PhysicWorld = World

val inputMultiplexer : InputMultiplexer
    get() = Gdx.input.inputProcessor as InputMultiplexer

class PixelAdventure : KtxGame<KtxScreen>() {

    private val spriteBatch : SpriteBatch by lazy { SpriteBatch() }
    private val assets : Assets by lazy { Assets() }
    private val gameProperties : GameProperties by lazy {
        val propertiesMap = ObjectMap<String,String>()
        Gdx.files.internal("game.properties").reader().use {
            PropertiesUtils.load(propertiesMap,it)
        }
        propertiesMap.toGameProperties()
    }

    private val physicWorld : PhysicWorld by lazy {
        World(vec2(0f,-30f),true).apply { autoClearForces = false }
    }
    private val preferences : Preferences by lazy {
        Gdx.app.getPreferences("pixel_adventure")
    }
    private val gamePreferences : GamePreferences by lazy {
        preferences.clear()
        GamePreferences(preferences)
    }

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer()
        addScreen(LoadingScreen(spriteBatch, physicWorld ,this,assets,gameProperties, gamePreferences ))
        setScreen<LoadingScreen>()
    }

    override fun render() {
        clearScreen(0f,0f,0f,0f)
        currentScreen.render(Gdx.graphics.deltaTime.coerceAtMost(1/30f))
    }

    override fun dispose() {
        spriteBatch.disposeSafely()
        assets.disposeSafely()
    }

    companion object{
        const val UNIT_SCALE = 1/16f
        val OBJECT_FIXTURES = mutableMapOf<GameObject,List<FixtureDefUserData>>()
        val isPhone : Boolean by lazy{
            Gdx.app.type == ApplicationType.iOS || Gdx.app.type == ApplicationType.Android
        }

    }
}
