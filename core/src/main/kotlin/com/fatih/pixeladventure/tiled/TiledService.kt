package com.fatih.pixeladventure.tiled

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Text
import com.fatih.pixeladventure.ecs.component.Tiled
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure.Companion.OBJECT_FIXTURES
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.fatih.pixeladventure.util.component1
import com.fatih.pixeladventure.util.component2
import com.fatih.pixeladventure.util.component3
import com.fatih.pixeladventure.util.component4
import com.github.quillraven.fleks.World
import ktx.app.gdxError
import ktx.box2d.body
import ktx.box2d.chain
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.id
import ktx.tiled.layer
import ktx.tiled.property
import ktx.tiled.propertyOrNull
import ktx.tiled.width
import ktx.tiled.x
import ktx.tiled.y
import kotlin.experimental.or
import kotlin.math.sin

class TiledService (
    private val physicWorld : PhysicWorld ,
    private val assets : Assets ,
    private val world : World
) :  GameEventListener {

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is MapChangeEvent -> {
                spawnEntities(gameEvent.tiledMap)
                spawnMapBoundaries(gameEvent.tiledMap)
            }
            else -> Unit
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

    private fun spawnMapBoundaries(tiledMap: TiledMap){
        physicWorld.body(BodyType.StaticBody){
            val vertices = floatArrayOf(
                0f,0f,
                0f,tiledMap.height.toFloat(),
                tiledMap.width.toFloat(),tiledMap.height.toFloat(),
                tiledMap.width.toFloat() ,0f
            )
            chain(vertices){
                userData = "mapBoundary"
            }
            val bottomVertices = floatArrayOf(
                -10f,0f,
                tiledMap.width.toFloat() + 10f,0f
            )
            chain(bottomVertices){
                userData = "bottomMapBoundary"
                isSensor = true
            }
        }
    }

    private fun spawnEntities(tiledMap: TiledMap){

        // spawn static ground bodies
        tiledMap.forEachCell { cell, x, y ->
            cell.tile?.objects?.forEach { collObj->
                spawnGroundObject(x,y,collObj)
            }
        }
        //spawn dynamic object bodies
        val trackLayer = tiledMap.layer("tracks")
        tiledMap.layer("objects").objects.forEach { spawnGameObjectEntity(it,trackLayer) }

    }

    private fun spawnTextEntities(mapObject: RectangleMapObject){
        val text = mapObject.propertyOrNull<String>("text")?: gdxError("Text not specified for $mapObject")
        world.entity {
            val xPos = mapObject.x * UNIT_SCALE
            val yPos = mapObject.y * UNIT_SCALE
            val width = mapObject.width * UNIT_SCALE
            val height = mapObject.height * UNIT_SCALE
            it += Text(text, Rectangle(xPos,yPos,width,height))
        }
    }

    private fun spawnGroundObject(x:Int, y:Int, collObj : MapObject){
        val body = createBody(BodyType.StaticBody, vec2(x.toFloat(),y.toFloat()),true)
        val fixtureDefUserData = fixtureDefinitionOf(collObj)
        body.createFixtures(listOf(fixtureDefUserData))
    }

    private fun spawnGameObjectEntity(mapObject: MapObject, trackLayer: MapLayer){
        if (mapObject is RectangleMapObject){
            spawnTextEntities(mapObject)
            return
        }
        if (mapObject !is TiledMapTileMapObject){
            gdxError("Unsupported mapObject $mapObject")
        }
        val tile = mapObject.tile
        val bodyType = tile.property<String>("bodyType","StaticBody")
        val gravityScale = tile.property<Float>("gravityScale",0f)
        val gameObjectStr = tile.property<String>("gameObject")
        val gameObject = GameObject.valueOf(gameObjectStr)
        val fixtureDefUserData = OBJECT_FIXTURES[gameObject]?: gdxError("No fixture definitions for ${gameObject.atlasKey}")
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE

        val body = createBody(BodyType.valueOf(bodyType), vec2(x,y),true).apply { this.gravityScale = gravityScale }
        body.createFixtures(fixtureDefUserData)

        world.entity {
            body.userData = it
            it += Tiled(mapObject.id,gameObject)
            it += Physic(body)
            configureEntityGraphic(it,tile,body,gameObject,assets,world)
            configureEntityTags(it,mapObject,tile,trackLayer)
            configureJump(it,tile)
            configureSpeed(it,tile)
            configureDamage(it,tile)
            configureState(it,tile,world,physicWorld)
            configureLife(it,tile)

        }
    }

    private fun createBody(bodyType: BodyType,position : Vector2,fixedRotation : Boolean) : Body{
        return physicWorld.body(bodyType){
            this.position.set(position)
            this.fixedRotation = fixedRotation
        }
    }

    private fun Body.createFixtures(fixtureDefUserData: List<FixtureDefUserData>) {
        fixtureDefUserData.forEach { it ->
            this.createFixture(it.fixtureDef).apply {
                this.userData = it.userData
                this.restitution
            }
            //it.fixtureDef.shape.dispose()
        }
    }

    companion object{

        data class FixtureDefUserData(val fixtureDef: FixtureDef, val userData : String)

        fun fixtureDefinitionOf(mapObject: MapObject) : FixtureDefUserData {
            val fixtureDef = when(mapObject){
                is RectangleMapObject -> rectangleFixtureDef(mapObject)
                is EllipseMapObject -> ellipseFixtureDef(mapObject)
                is PolygonMapObject -> polygonFixtureDef(mapObject)
                is PolylineMapObject -> polylineFixtureDef(mapObject)
                else -> gdxError("Unsupported mapObject $mapObject")
            }
            val userData = mapObject.property("userData","")
            fixtureDef.apply {
                friction = mapObject.property("friction",0f)
                restitution = mapObject.property("restitution",0f)
                isSensor = mapObject.property("isSensor",false)
                density = mapObject.property("density",0f)
                val gameObject = GameObject.valueOf(mapObject.property<String>("gameObject","GROUND"))
                filter.categoryBits = gameObject.categoryBit
                filter.maskBits = gameObject.maskBits
                if (userData == "footFixture") filter.maskBits = filter.maskBits or PLATFORM_BIT
            }
            return FixtureDefUserData(fixtureDef,userData)
        }


        private fun polylineFixtureDef(mapObject: PolylineMapObject) : FixtureDef {
            return polygonFixtureDef(mapObject.x,mapObject.y,mapObject.polyline.vertices,false)
        }

        private fun polygonFixtureDef(mapObject: PolygonMapObject) : FixtureDef {
            return polygonFixtureDef(mapObject.x,mapObject.y,mapObject.polygon.vertices,true)
        }

        private fun polygonFixtureDef(polyX : Float,polyY : Float,polyVertices : FloatArray,loop : Boolean) : FixtureDef {
            val x = polyX * UNIT_SCALE
            val y = polyY * UNIT_SCALE
            val vertices = FloatArray(polyVertices.size){vertexIdx ->
                if (vertexIdx % 2 == 0){
                    x + polyVertices[vertexIdx] * UNIT_SCALE
                }else{
                    y + polyVertices[vertexIdx] * UNIT_SCALE
                }
            }

            return FixtureDef().apply {
                shape =
                    ChainShape().apply {
                        if (loop){
                            createLoop(vertices)
                        }else{
                            createChain(vertices)
                        }
                    }
            }
        }

        private fun rectangleFixtureDef(mapObject: RectangleMapObject) : FixtureDef {
            val(rectX,rectY,rectW,rectH) = mapObject.rectangle
            val boxW = rectW * UNIT_SCALE / 2f
            val boxH = rectH * UNIT_SCALE / 2f
            /*val vertices = arrayOf(
                vec2(boxX, boxY),
                vec2(boxX + boxW, boxY),
                vec2(boxX + boxW, boxY + boxH),
                vec2(boxX, boxY + boxH)
            ) */
            return  FixtureDef().apply {
                shape = PolygonShape().apply {
                    setAsBox(boxW,boxH, vec2(rectX * UNIT_SCALE + boxW,rectY * UNIT_SCALE + boxH),0f)
                }
            }
        }

        private fun ellipseFixtureDef(mapObject: EllipseMapObject) : FixtureDef {
            val (x,y,w,h) = mapObject.ellipse
            val ellipseX = x * UNIT_SCALE
            val ellipseY = y * UNIT_SCALE
            val ellipseW = w * UNIT_SCALE / 2f
            val ellipseH = h * UNIT_SCALE / 2f
            return if (MathUtils.isEqual(ellipseW, ellipseH, 0.1f)){
                FixtureDef().apply {
                    shape = CircleShape().apply {
                        position = vec2(ellipseX + ellipseW , ellipseY + ellipseH)
                        radius = ellipseW
                    }
                }
            }else{
                val numVertices = 20
                val angleStep = MathUtils.PI2 / numVertices
                val vertices = Array(numVertices){vertexIdx ->
                    val angle = vertexIdx * angleStep
                    val offsetX = ellipseW * MathUtils.cos(angle)
                    val offsetY = ellipseH * sin(angle)
                    vec2(ellipseX + ellipseW + offsetX,ellipseY + ellipseH + offsetY)
                }
                FixtureDef().apply {
                    shape = ChainShape().apply {
                        createLoop(vertices)
                    }
                }
            }
        }
    }


}
