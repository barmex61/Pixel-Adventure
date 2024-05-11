package com.fatih.pixeladventure.util

import com.badlogic.gdx.utils.ObjectMap

inline fun <reified T> ObjectMap<String,String>.getOrDefault(gamePropKey:GamePropertyKey,defaultValue : T) : T {
    return this.getOrDefault(gamePropKey.key,defaultValue)
}


inline fun <reified T> ObjectMap<String,String>.getOrDefault(key:String,defaultValue : T) : T {
    val strValue = this.get(key) ?: return defaultValue
    return when(T::class){
        Int::class -> strValue.toInt() as T
        Float::class -> strValue.toFloat() as T
        Boolean::class -> strValue.toBoolean() as T
        else -> strValue as T
    }
}
