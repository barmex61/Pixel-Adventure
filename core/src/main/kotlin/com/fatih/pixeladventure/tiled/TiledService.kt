package com.fatih.pixeladventure.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
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
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.pixeladventure.ai.AiEntity
import com.fatih.pixeladventure.ai.IdleState
import com.fatih.pixeladventure.ecs.component.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Damage
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.event.MapChangeEvent
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Tiled
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.game.PixelAdventure.Companion.OBJECT_FIXTURES
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.EntityModel
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.TextureAtlasAsset
import com.fatih.pixeladventure.util.animation
import com.fatih.pixeladventure.util.component1
import com.fatih.pixeladventure.util.component2
import com.fatih.pixeladventure.util.component3
import com.fatih.pixeladventure.util.component4
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.app.gdxError
import ktx.box2d.body
import ktx.collections.GdxFloatArray
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.id
import ktx.tiled.layer
import ktx.tiled.property
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
            is MapChangeEvent -> spawnEntities(gameEvent.tiledMap)
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

    private fun spawnEntities(tiledMap: TiledMap){
        // spawn static ground bodies
        tiledMap.forEachCell { cell, x, y ->
            cell.tile?.objects?.forEach { collObj->
                spawnGroundObject(x,y,collObj)
            }
        }
        //spawn dynamic object bodies
        tiledMap.layer("objects").objects.forEach { spawnGameObjectEntity(it) }

        world.family { all(EntityTag.FOLLOW_TRACK) }.forEach {

            val (mapObjectId,_,mapObjectBoundary) = it[Tiled]
            val rectVertices = GdxFloatArray(
                floatArrayOf(
                    mapObjectBoundary.x , mapObjectBoundary.y,
                    mapObjectBoundary.x + mapObjectBoundary.width,mapObjectBoundary.y,
                    mapObjectBoundary.x + mapObjectBoundary.width, mapObjectBoundary.y + mapObjectBoundary.height,
                    mapObjectBoundary.x , mapObjectBoundary.y + mapObjectBoundary.height
                )
            )
            val trackVectorList = tiledMap.layer("chainsaw_tracks").trackVerticesByBoundary(mapObjectId,rectVertices)
            it.configure { it += Track(trackVectorList) }
        }
    }

    private fun MapLayer.trackVerticesByBoundary(mapObjectId : Int, rectVertices : GdxFloatArray) : List<Vector2> {
        objects.forEach { layerObject->
            if (layerObject !is PolylineMapObject && layerObject !is PolygonMapObject){
                gdxError("Only Polyline map objects are supported for tracks $layerObject")
            }
            val lineVertices = when(layerObject){
                is PolylineMapObject -> GdxFloatArray(layerObject.polyline.transformedVertices)
                is PolygonMapObject ->GdxFloatArray(layerObject.polygon.transformedVertices)
                else -> gdxError("Only Polyline map objects are supported for tracks $layerObject")

            }
            if (Intersector.intersectPolygons(lineVertices,rectVertices)){
                val trackPoints = mutableListOf<Vector2>()
                for (i in 0..<lineVertices.size step 2){
                    val vertexX = lineVertices[i] * UNIT_SCALE
                    val vertexY = lineVertices[i+1] * UNIT_SCALE
                    trackPoints += Vector2(vertexX,vertexY)
                }
                return trackPoints
            }
        }
        gdxError("There is no related track for $mapObjectId")
    }

    private fun spawnGroundObject(x:Int, y:Int, collObj : MapObject){
        val body = createBody(BodyType.StaticBody, vec2(x.toFloat(),y.toFloat()),true)
        val fixtureDefUserData = fixtureDefinitionOf(collObj)
        body.createFixtures(listOf(fixtureDefUserData))
    }

    private fun spawnGameObjectEntity(mapObject: MapObject){
        if (mapObject !is TiledMapTileMapObject){
            gdxError("Unsupported mapObject $mapObject")
        }
        val tile = mapObject.tile
        val bodyType = tile.property<String>("bodyType","StaticBody")
        val gameObjectStr = tile.property<String>("gameObject")
        val gameObject = GameObject.valueOf(gameObjectStr)
        val fixtureDefUserData = OBJECT_FIXTURES[gameObject]?: gdxError("No fixture definitions for ${gameObject.atlasKey}")
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE

        val body = createBody(BodyType.valueOf(bodyType), vec2(x,y),true)
        body.createFixtures(fixtureDefUserData)

        world.entity {
            body.userData = it
            it += Tiled(mapObject.id,gameObject, Rectangle(mapObject.x,mapObject.y,mapObject.width,mapObject.height))

            val gravityScale = tile.property<Float>("gravityScale",1f)
            it += Physic(body.apply { this.gravityScale = gravityScale })

            val startAnimType = AnimationType.valueOf(tile.property("startAnimType","IDLE"))
            it += Graphic(sprite(gameObject,startAnimType,body.position))

            val entityTags = tile.property<String>("entityTags","")
            if (entityTags.isNotBlank()){
                val tags = entityTags.split(",").map { splitEntityTag -> EntityTag.valueOf(splitEntityTag)}
                it += tags
            }

            val hasAnimation = tile.property<Boolean>("hasAnimation",false)
            if (hasAnimation){
                it += Animation(frameDuration = tile.property<Float>("animFrameDuration"))
                world.animation(it,startAnimType)
            }

            val jumpHeight = tile.property<Float>("jumpHeight",0f)
            if (jumpHeight > 0f){
                it += Jump(jumpHeight)
            }

            val speed = tile.property<Float>("speed",0f)
            if (speed > 0f ){
                val timeToMax = tile.property<Float>("timeToMax",0.1f)
                it += Move(timeToMax = timeToMax, max = speed)
            }

            val damage = tile.property<Int>("damage",0)
            if (damage > 0){
                it += Damage(damage)
            }

            val hasState = tile.property<Boolean>("hasState",false)
            if (hasState){
                it += State(AiEntity(it,world),IdleState)
            }

            val life = tile.property<Int>("life",0)
            if (life > 0){
                it += Life(life)
            }

        }

    }

    private fun sprite(gameObject: GameObject, animationType: AnimationType,startPosition : Vector2): Sprite {
        val atlas = assets[TextureAtlasAsset.GAMEOBJECT]
        val regions = atlas.findRegions("${gameObject.atlasKey}/${animationType.atlasKey}") ?:
            gdxError("There are no regions for $gameObject and $animationType")
        val firstFrame = regions.first()
        val w = firstFrame.regionWidth * UNIT_SCALE
        val h = firstFrame.regionHeight * UNIT_SCALE
        return Sprite(firstFrame).apply {
            setPosition(startPosition.x,startPosition.y)
            setSize(w,h)
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
                else -> gdxError("Unsupported mapobject $mapObject")
            }
            val userData = mapObject.property("userData","")
            fixtureDef.apply {
                friction = mapObject.property("friction",0f)
                restitution = mapObject.property("restitution",0f)
                isSensor = mapObject.property("isSensor",false)
                density = mapObject.property("density",0f)
                val entityModel = EntityModel.valueOf(mapObject.property<String>("entityModel","GROUND"))
                filter.categoryBits = entityModel.categoryBit
                filter.maskBits = entityModel.maskBits
                if (userData == "player_foot") filter.maskBits = filter.maskBits or PLATFORM_BIT
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
                shape = ChainShape().apply {
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
