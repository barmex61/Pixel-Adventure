package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.fatih.pixeladventure.ai.FruitState
import com.fatih.pixeladventure.ai.PlayerState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.Collectable
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Flash
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem.Companion.DEBUG_RECT
import com.fatih.pixeladventure.event.CollectItemEvent
import com.fatih.pixeladventure.event.GameEvent
import com.fatih.pixeladventure.event.GameEventListener
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.GROUND_BIT
import com.fatih.pixeladventure.util.GameObject
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import ktx.box2d.query
import kotlin.math.sqrt

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
    private val audioService: AudioService = World.inject()
): IteratingSystem(family = World.family { all(Jump) }) , GameEventListener{

    override fun onTickEntity(entity: Entity) {
        val jumpComps = entity[Jump]
        val (body,_) = entity[Physic]
        var (maxHeight , lowerFeet,upperFeet,buffer,doubleJump,jumpOnGround,fly,flyTimer) = entity[Jump]

        if (fly ){
            jumpComps.flyTimer -= deltaTime
            println(jumpComps.flyTimer)
            if (flyTimer <= 0f){
                jumpComps.flyTimer = 2f
                jumpComps.fly = false
            }
            if (buffer == Jump.JUMP_BUFFER_TIME){
                val gravityY = if (physicWorld.gravity.y == 0f) 1f else physicWorld.gravity.y
                body.setLinearVelocity(body.linearVelocity.x, sqrt(maxHeight * -gravityY))
            }
            return
        }
        if (doubleJump){
            applyJumpForce(jumpComps,body,maxHeight * 0.8f)
            jumpComps.doubleJump = false
            entity[State].stateMachine.changeState(PlayerState.DOUBLE_JUMP)
            return
        }

        if (buffer == 0f ){
            return
        }

        jumpComps.buffer = (jumpComps.buffer -deltaTime).coerceAtLeast(0f)
        if (!MathUtils.isEqual(body.linearVelocity.y,0f,2f)){
            return
        }

        val lowerX = body.position.x + lowerFeet.x - 0.15f
        val lowerY = body.position.y + lowerFeet.y - 0.15f
        val upperX = body.position.x + upperFeet.x + 0.15f
        val upperY = body.position.y + upperFeet.y + 0.15f

        DEBUG_RECT.set(lowerX,lowerY,upperX-lowerX,upperY-lowerY)

        physicWorld.query(lowerX,lowerY,upperX,upperY){fixture ->
            val categoryBit = fixture.filterData.categoryBits
            val userData = fixture.userData
            if ((categoryBit == GROUND_BIT && userData == "cantJump" && jumpOnGround) ||
                (categoryBit == GROUND_BIT && userData == "canJump") ||
                (categoryBit == ROCK_HEAD_BIT && userData == "hitbox") ||
                categoryBit == PLATFORM_BIT){
                if (categoryBit == GROUND_BIT && userData == "cantJump" && jumpOnGround) jumpComps.jumpOnGround = false
                applyJumpForce(jumpComps,body,maxHeight)
                audioService.play(SoundAsset.JUMP)
                return@query false
            }

            return@query true
        }
    }

    private fun applyJumpForce(jumpComp : Jump,body: Body,maxHeight:Float){
        jumpComp.buffer = 0f
        val gravityY = if (physicWorld.gravity.y == 0f) 1f else physicWorld.gravity.y
        body.setLinearVelocity(body.linearVelocity.x, sqrt(2 * maxHeight * -gravityY))
    }

    override fun onEvent(gameEvent: GameEvent) {
        when(gameEvent){
            is CollectItemEvent->{
                val collectableEntity = gameEvent.collectEntity
                collectableEntity.configure {
                    it -= EntityTag.COLLECTABLE
                }
                collectableEntity[State].stateMachine.changeState(FruitState.HIT_RESPAWN)
                when(collectableEntity[Collectable].name){
                    GameObject.CHERRY.name -> gameEvent.playerEntity[Jump].doubleJump = true
                    GameObject.BANANA.name -> gameEvent.playerEntity[Move].max += 1f
                    GameObject.MELON.name -> gameEvent.playerEntity[Jump].jumpOnGround = true
                    GameObject.PINEAPPLE.name -> gameEvent.playerEntity[Jump].fly = true
                    GameObject.KIWI.name ->{
                        gameEvent.playerEntity.configure {
                            if (!(it has Invulnarable) && !(it has Blink)){
                                it += Invulnarable(3.5f)
                                it += Blink(3.5f,0.075f)
                            }
                        }
                    }

                    else -> Unit
                }
            }
            else -> Unit
        }
    }

}
