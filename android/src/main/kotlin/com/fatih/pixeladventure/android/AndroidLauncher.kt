package com.fatih.pixeladventure.android

import android.content.Context
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidAudio
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio
import com.fatih.pixeladventure.game.PixelAdventure


/** Launches the Android application. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(PixelAdventure(), AndroidApplicationConfiguration().apply {
            // Configure your application here.
            useImmersiveMode = true // Recommended, but not required.
            useGL30 = true
            maxSimultaneousSounds
        })

    }

    override fun createAudio(context: Context?, config: AndroidApplicationConfiguration?): AndroidAudio {
        return AsynchronousAndroidAudio(context, config)
    }
}
