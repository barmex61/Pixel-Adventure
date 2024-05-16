package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.Animation
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.animation
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.box2d.rayCast
import ktx.math.vec2
import kotlin.math.pow

data class AiEntity(val entity: Entity,val world: World,val physicWorld: PhysicWorld) {

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP) = world.animation(entity,animationType,playMode)

    fun state(state : GameObjectState){
        with(world){entity[State]}.stateMachine.changeState(state)
    }

    fun hasAggroTarget(): Entity? = with(world) {
        return@with entity[Aggro].aggroEntities.firstOrNull()
    }


    fun inRange(range: Float, targetEntity: Entity): Boolean = with(world){
        val (_,targetCenter) = targetEntity[Graphic]
        return@with inRange(targetCenter,range)
    }

    fun inRange(location:Vector2,tolerance:Float) : Boolean = with(world){
        val (_,center) = entity[Graphic]
        val diffX = (center.x - location.x)
        val diffY = (center.y - location.y)
        return@with diffX.pow(2) + diffY.pow(2) < tolerance.pow(2)
    }

    fun move(targetEntity: Entity) = with(world) {
        val (_,center) = entity[Graphic]
        val (_,targetCenter) = targetEntity[Graphic]

        val diffX = (center.x - targetCenter.x)
        val diffY = (center.y - targetCenter.y)

        entity[Move].direction = when{
            entity[Aggro].verticalCollision -> {
                when{
                    diffY < 0f -> MoveDirection.UP
                    diffY > 0f -> MoveDirection.DOWN
                    else -> entity[Move].direction
                }
            }
            entity[Aggro].horizontalCollision ->{
                when{
                    diffX > 0f -> MoveDirection.LEFT
                    diffX < 0f -> MoveDirection.RIGHT
                    else -> entity[Move].direction
                }
            }
            else -> entity[Move].previousDirection
        }
    }

    fun isAnimationDone(): Boolean = with(world){
        val animComp = entity[Animation]
        animComp.gdxAnimation!!.isAnimationFinished(animComp.timer)
    }


    fun isPathBlocked(targetEntity: Entity) : Boolean = with(world){
        val start = entity[Graphic].center
        val end = targetEntity[Graphic].center
        var blocked = false
        physicWorld.rayCast(start,end){fixture, point, normal, fraction ->
            if (fixture.body.type == BodyDef.BodyType.StaticBody){
                blocked = true
                return@rayCast 0f
            }
            return@rayCast -1f
        }

        return@with blocked
    }
}
