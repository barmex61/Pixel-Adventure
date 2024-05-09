package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.physics.box2d.BodyDef
import com.fatih.pixeladventure.GameEvent
import com.fatih.pixeladventure.GameEventListener
import com.fatih.pixeladventure.MapChangeEvent
import com.fatih.pixeladventure.PhysicWorld
import com.fatih.pixeladventure.PixelAdventure.Companion.UNIT_SCALE
import com.fatih.pixeladventure.component1
import com.fatih.pixeladventure.component2
import com.fatih.pixeladventure.component3
import com.fatih.pixeladventure.component4
import com.fatih.pixeladventure.ecs.component.Physic
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.body
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.isNotEmpty
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
        tiledMap.forEachCell { cell, x, y ->
            cell.tile?.objects?.forEach { collObj->
                spawnEntity(x,y,collObj)
            }
        }
    }

    private fun spawnEntity(x:Int,y:Int,collObj : MapObject){
        when(collObj){
            is RectangleMapObject ->{
                val (rectX,rectY,rectW,rectH) = collObj.rectangle
                val body = physicWorld.body(BodyDef.BodyType.StaticBody){
                    position.set(x.toFloat() + rectW * UNIT_SCALE/ 2f,y.toFloat() + rectH * UNIT_SCALE / 2f )
                    box(rectW * UNIT_SCALE,rectH * UNIT_SCALE, vec2(rectX * UNIT_SCALE,rectY * UNIT_SCALE))
                    fixedRotation = true
                }
                world.entity {
                    body.userData = it
                    it += Physic(body)
                }
            }
        }
    }

}
