package com.fatih.pixeladventure.util

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import ktx.app.gdxError
import ktx.math.vec2


fun fixtureDefinitionOf(mapObject: MapObject) : FixtureDef {
    return when(mapObject){
        is RectangleMapObject ->{
            val(rectX,rectY,rectW,rectH) = mapObject.rectangle
            val boxW = rectW * UNIT_SCALE / 2f
            val boxH = rectH * UNIT_SCALE / 2f
            FixtureDef().apply {
                shape = PolygonShape().apply { setAsBox(boxW,boxH, vec2(rectX * UNIT_SCALE + boxW,rectY * UNIT_SCALE + boxH),0f) }
            }

        }
        is EllipseMapObject ->{
            val (x,y,w,h) = mapObject.ellipse
            val ellipseW = w * UNIT_SCALE / 2f
            val ellipseH = h * UNIT_SCALE / 2f
            FixtureDef().apply {
                shape = CircleShape().apply {
                    radius = w * UNIT_SCALE / 2f
                    position = vec2(x * UNIT_SCALE + ellipseW,y * UNIT_SCALE + ellipseH)
                }
            }
        }
        else -> gdxError("Unsupported mapobject $mapObject")
    }
}
