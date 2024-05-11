package com.fatih.pixeladventure.util

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.set
import kotlin.test.Test

class GamePropertiesTest{

    @Test
    fun testGetOrDefaultProperty(){
        val testMap = ObjectMap<String,String>().apply {
            set("debugPhysic","true")
            set("enableProfiling","true")
            set("musicVolume","0.025f")
            set("soundVolume","0.05")

        }
        val gameProperties = testMap.toGameProperties()

        kotlin.test.assertEquals(true, gameProperties.debugPhysic)
        kotlin.test.assertEquals(true, gameProperties.enableProfiling)
        kotlin.test.assertEquals(0.025f, gameProperties.musicVolume)
        kotlin.test.assertEquals(0.05f, gameProperties.soundVolume)
    }
}
