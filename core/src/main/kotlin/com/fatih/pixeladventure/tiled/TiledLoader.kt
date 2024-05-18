package com.fatih.pixeladventure.tiled

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.XmlReader
import ktx.tiled.set

class TiledLoader(fileHandleResolver: FileHandleResolver) : TmxMapLoader(fileHandleResolver) {

    override fun loadObject(map: TiledMap, objects: MapObjects, element: XmlReader.Element, heightInPixels: Float) {
        super.loadObject(map, objects, element, heightInPixels)
        val lastObject = objects.last()
        if (lastObject.name == "TEXT"){
            element.getChildByName("text")?.text?.let { objectText->
                lastObject.properties["text"] = objectText
            }
        }
    }
}
