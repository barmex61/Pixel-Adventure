package com.fatih.pixeladventure.util

import com.badlogic.gdx.Preferences
import ktx.collections.GdxArray
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set

class GamePreferences(private val preferences: Preferences) {

    init {
        storeUnlockedMap(MapAsset.TUTORIAL)
    }

    fun storeUnlockedMap(mapAsset: MapAsset) {
        preferences.flush {
            this[UNLOCKED_MAPS] = this[UNLOCKED_MAPS,GdxArray<MapAsset>()] + mapAsset
        }
    }

    fun loadUnlockedMaps(): Set<MapAsset> {
        preferences.flush {
            return preferences[UNLOCKED_MAPS,GdxArray<MapAsset>()].toSet()
        }
    }

    fun clearPreferences(){
        preferences.flush {
            preferences.clear()
        }
    }

    companion object{
        private val UNLOCKED_MAPS = "unlocked-maps"
    }
}
