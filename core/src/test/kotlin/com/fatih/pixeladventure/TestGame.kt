package com.fatih.pixeladventure

import ktx.app.KtxGame
import ktx.app.KtxScreen

class TestGame(val changeScreen: () -> KtxScreen) : KtxGame<KtxScreen>() {

    override fun create() {
        val testScreen = changeScreen()
        addScreen(testScreen)
        setScreen<KtxScreen>()
    }
}
