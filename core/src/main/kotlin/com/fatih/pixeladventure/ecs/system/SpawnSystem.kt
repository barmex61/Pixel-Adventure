package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Ellipse
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.BodyDef
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import com.fatih.pixeladventure.util.component1
import com.fatih.pixeladventure.util.component2
import com.fatih.pixeladventure.util.component3
import com.fatih.pixeladventure.util.component4
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure.Companion.OBJECT_FIXTURES
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.fixtureDefinitionOf
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ktx.box2d.body
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.property
import ktx.tiled.shape
import ktx.tiled.width

class SpawnSystem (
    private val physicWorld : PhysicWorld = inject()
): IntervalSystem(enabled = false) , GameEventListener {

    override fun onTick() = Unit

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent -> spawnEntities(gameEvent.tiledMap)
        }
    }

    private inline fun TiledMap.forEachCell(action : (Cell,Int,Int) -> Unit){
        val width = this.width
        val height = this.height
        this.layers.filterIsInstance<TiledMapTileLayer>().forEach {layer->
            for (x in 0..width){
                for (y in 0..height){
                    val cell = layer.getCell(x,y)
                    cell?.let { action(it,x,y) }
                }
            }
        }
    }

    private fun spawnEntities(tiledMap: TiledMap){
        // spawn static ground bodies
        tiledMap.forEachCell { cell, x, y ->
            cell.tile?.objects?.forEach { collObj->
                spawnGroundEntity(x,y,collObj)
            }
        }
        //spawn dynamic object bodies
        tiledMap.layers
            .filter { it !is TiledMapTileLayer }
            .forEach { mapLayer ->
            mapLayer.objects
                .forEach {
                    spawnGameObjectEntity(it)
                }
        }
    }

    private fun spawnGameObjectEntity(mapObject: MapObject){
        if (mapObject !is TiledMapTileMapObject){
            gdxError("Unsupported mapObject $mapObject")
        }
        val gameObjectStr = mapObject.tile.property<String>("GameObject")
        val fixtureDefs = OBJECT_FIXTURES[GameObject.valueOf(gameObjectStr)]?: gdxError("No fixture definitions for ${gameObjectStr}")
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE


        val body = physicWorld.body(BodyDef.BodyType.DynamicBody){
            position.set(x ,y )
            fixedRotation = true
        }
        fixtureDefs.forEach { fixtureDef ->
            body.createFixture(fixtureDef)
            fixtureDef.shape.dispose()
        }

    }

    private fun spawnGroundEntity(x:Int, y:Int, collObj : MapObject){
        when(collObj){
            is RectangleMapObject ->{
                val body = physicWorld.body(BodyDef.BodyType.StaticBody){
                    position.set(x.toFloat() ,y.toFloat() )
                    fixedRotation = true
                }

                val fixtureDef = fixtureDefinitionOf(collObj)
                body.createFixture(fixtureDef)
                fixtureDef.shape.dispose()
                world.entity {
                    body.userData = it
                    it += Physic(body)
                }
            }
        }
    }

}
