package com.fatih.pixeladventure.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.fatih.pixeladventure.ai.AiEntity
import com.fatih.pixeladventure.ai.GameObjectState
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Damage
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Teleport
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import com.fatih.pixeladventure.util.Assets
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.TextureAtlasAsset
import com.fatih.pixeladventure.util.animation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.World
import ktx.app.gdxError
import ktx.collections.GdxFloatArray
import ktx.math.vec2
import ktx.tiled.height
import ktx.tiled.id
import ktx.tiled.property
import ktx.tiled.propertyOrNull
import ktx.tiled.width


private fun sprite(gameObject: GameObject, animationType: AnimationType, startPosition : Vector2,assets: Assets): Sprite {
    val animationPath = "${gameObject.atlasKey}/${animationType.atlasKey}"
    val atlas = assets[TextureAtlasAsset.GAMEOBJECT]
    val regions = atlas.findRegions(animationPath) ?:
    gdxError("There are no regions for $gameObject and $animationType")
    val firstFrame = regions.first()
    val w = firstFrame.regionWidth * UNIT_SCALE
    val h = firstFrame.regionHeight * UNIT_SCALE
    return Sprite(firstFrame).apply {
        setPosition(startPosition.x,startPosition.y)
        setSize(w,h)
    }
}

fun EntityCreateContext.configureEntityGraphic(entity: Entity,tile: TiledMapTile,body: Body,gameObject: GameObject,assets: Assets,world: World){
    val startAnimType = AnimationType.valueOf(tile.property("startAnimType","NONE"))
    if (startAnimType != AnimationType.NONE){
        entity += Graphic(sprite(gameObject,startAnimType,body.position,assets))
        configureAnimation(entity,tile,world,startAnimType)
    }
}

fun EntityCreateContext.configureEntityTags(
    entity: Entity,
    mapObject: TiledMapTileMapObject,
    tile: TiledMapTile,
    trackLayer : MapLayer
){
    val entityTags = mapObject.propertyOrNull<String>("entityTags")?:tile.property("entityTags","")
    if (entityTags.isNotBlank()){
        val tags = entityTags.split(",").map { splitEntityTag -> EntityTag.valueOf(splitEntityTag)}
        entity += tags
    }
    if (entity has EntityTag.HAS_TRACK){
        entity += trackLayer.trackCompOf(mapObject)
    }
    if (entity has EntityTag.HAS_AGGRO){
        entity += Aggro(sourceLocation = entity[Graphic].center.cpy())
    }
    if (entity has EntityTag.PLAYER){
        entity += Teleport(Vector2(mapObject.x * UNIT_SCALE,mapObject.y * UNIT_SCALE))
    }
}

fun EntityCreateContext.configureAnimation(entity: Entity, tile: TiledMapTile, world: World,startAnimType : AnimationType) {
    entity += Animation(frameDuration = tile.property<Float>("animFrameDuration"))
    world.animation(entity,startAnimType)
}

fun EntityCreateContext.configureJump(entity: Entity, tile: TiledMapTile){
    val jumpHeight = tile.property<Float>("jumpHeight",0f)
    if (jumpHeight > 0f){
        entity += Jump(jumpHeight)
    }
}

fun EntityCreateContext.configureSpeed(entity: Entity, tile: TiledMapTile){
    val speed = tile.property<Float>("speed",0f)
    if (speed > 0f ){
        val timeToMax = tile.property<Float>("timeToMax",0.1f)
        entity += Move(timeToMax = timeToMax, max = speed)
    }
}

fun EntityCreateContext.configureDamage(entity: Entity, tile: TiledMapTile){
    val damage = tile.property<Int>("damage",0)
    if (damage > 0){
        entity += Damage(damage)
    }
}

fun EntityCreateContext.configureState(entity: Entity, tile: TiledMapTile,world: World){
    val initialState = tile.property<String>("gameObjectState","NONE")
    if (initialState.isNotBlank() && initialState != "NONE"){
        entity += State(AiEntity(entity,world), GameObjectState.valueOf(initialState))
    }
}

fun EntityCreateContext.configureLife(entity: Entity, tile: TiledMapTile){
    val life = tile.property<Int>("life",0)
    if (life > 0){
        entity += Life(life)
    }
}

private fun MapLayer.trackCompOf(mapObject: TiledMapTileMapObject) : Track{
    objects.forEach{layerObject ->
        val lineVertices = when(layerObject){
            is PolygonMapObject -> GdxFloatArray(layerObject.polygon.transformedVertices)
            is PolylineMapObject -> GdxFloatArray(layerObject.polyline.transformedVertices)
            else -> gdxError("Only polyline and polygon vertices are supported $layerObject")
        }
        val rectVertices = GdxFloatArray(
            floatArrayOf(
                mapObject.x,mapObject.y,
                mapObject.x + mapObject.width,mapObject.y,
                mapObject.x + mapObject.width,mapObject.y + mapObject.height,
                mapObject.x,mapObject.y + mapObject.height
            )
        )
        if (Intersector.intersectPolygons(lineVertices,rectVertices)){
            val trackPoints = mutableListOf<Vector2>()
            for (i in 0 until lineVertices.size step 2){
                val vertexX = lineVertices[i] * UNIT_SCALE
                val vertexY = lineVertices[i+1] * UNIT_SCALE
                trackPoints += vec2(vertexX,vertexY)
            }
            return Track(trackPoints)
        }
    }
    gdxError("There is no related tracks for MapObject ${mapObject.id}")
}
