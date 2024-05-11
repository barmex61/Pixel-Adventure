package com.fatih.pixeladventure.util

import com.badlogic.gdx.utils.ObjectMap

fun ObjectMap<String,String>.toGameProperties() = GameProperties (
    soundVolume = getOrDefault("soundVolume",0.05f),
    musicVolume = getOrDefault("musicVolume",0.025f),
    enableProfiling = getOrDefault("enableProfiling",true),
    debugPhysic = getOrDefault("debugPhysic",true)
)

data class GameProperties(
    val debugPhysic : Boolean,
    val enableProfiling : Boolean,
    val musicVolume : Float,
    val soundVolume : Float
)

private inline fun <reified T> ObjectMap<String,String>.getOrDefault(key:String,defaultValue : T) : T {
    val strValue = this.get(key) ?: return defaultValue
    return when(T::class){
        Int::class -> strValue.toInt() as T
        Float::class -> strValue.toFloat() as T
        Boolean::class -> strValue.toBoolean() as T
        else -> strValue as T
    }
}


