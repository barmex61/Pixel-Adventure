package com.fatih.pixeladventure

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.fatih.pixeladventure.screen.MenuViewTestScreen
import com.fatih.pixeladventure.screen.ParallaxTestScreen

fun main(){
    Lwjgl3Application(TestGame(::MenuViewTestScreen),Lwjgl3ApplicationConfiguration().apply {
        setTitle("Pixel Adventure Test")
        setWindowedMode(640,360)
    })
}
