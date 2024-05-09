package com.fatih.pixeladventure

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.fatih.pixeladventure.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely

typealias PhysicWorld = World

class PixelAdventure : KtxGame<KtxScreen>() {

    private val spriteBatch : SpriteBatch by lazy { SpriteBatch() }
    private val assets : Assets by lazy { Assets() }

    override fun create() {
        addScreen(GameScreen(spriteBatch,assets))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        spriteBatch.disposeSafely()
        assets.dispose()
    }

    companion object{
        const val UNIT_SCALE = 1/16f
    }
}

