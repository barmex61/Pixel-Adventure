package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Ellipse
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Graphic
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
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.TextureAtlasAsset
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
    private val physicWorld : PhysicWorld = inject(),
    private val assets : Assets = inject()
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


    private fun spawnGroundEntity(x:Int, y:Int, collObj : MapObject){
        val body = createBody(BodyType.StaticBody, vec2(x.toFloat(),y.toFloat()),true)
        val fixtureDef = fixtureDefinitionOf(collObj)
        body.createFixtures(listOf(fixtureDef))
    }

    private fun spawnGameObjectEntity(mapObject: MapObject){
        if (mapObject !is TiledMapTileMapObject){
            gdxError("Unsupported mapObject $mapObject")
        }
        val gameObjectStr = mapObject.tile.property<String>("GameObject")
        val gameObjectId = GameObject.valueOf(gameObjectStr)
        val fixtureDefs = OBJECT_FIXTURES[gameObjectId]?: gdxError("No fixture definitions for $gameObjectStr")
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE

        val body = createBody(BodyType.DynamicBody, vec2(x,y),true)
        body.createFixtures(fixtureDefs)

        world.entity {
            body.userData = it

            it += Physic(body)
            it += Graphic(sprite(gameObjectId,"idle"))

            if (gameObjectId == GameObject.FROG ){
                it += EntityTag.PLAYER
            }
        }

    }

    private fun sprite(gameObjectId: GameObject, animationType: String): Sprite {
        val atlas = assets[TextureAtlasAsset.GAMEOBJECT]
        val regions = atlas.findRegions("${gameObjectId.name.lowercase()}/${animationType}") ?:
            gdxError("There are no regions for $gameObjectId and $animationType")
        val firstFrame = regions.first()
        val w = firstFrame.regionWidth * UNIT_SCALE
        val h = firstFrame.regionHeight * UNIT_SCALE
        return Sprite(firstFrame).apply {
            setSize(w,h)
        }
    }


    private fun createBody(bodyType: BodyType,position : Vector2,fixedRotation : Boolean) : Body{
        return physicWorld.body(bodyType){
            this.position.set(position)
            this.fixedRotation = fixedRotation
        }
    }

    private fun Body.createFixtures(fixtureDefs : List<FixtureDef>) {
        fixtureDefs.forEach { fixtureDef ->
            this.createFixture(fixtureDef)
            fixtureDef.shape.dispose()
        }
    }

}
