package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.fatih.pixeladventure.ai.PlayerState
import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.Fly
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.system.PhysicDebugRenderSystem.Companion.DEBUG_RECT
import com.fatih.pixeladventure.event.EndFruitEffectEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.FruitDrawable
import com.fatih.pixeladventure.util.GROUND_BIT
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.PLAYER_BIT
import com.fatih.pixeladventure.util.ROCK_HEAD_BIT
import com.fatih.pixeladventure.util.SPIKE_HEAD_BIT
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import ktx.box2d.query
import kotlin.math.sqrt

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
    private val audioService: AudioService = World.inject()
): IteratingSystem(family = family { all(Jump) }) {

    private var fireFruitEventOnce : Boolean = true

    override fun onTickEntity(entity: Entity) {
        val jumpComps = entity[Jump]
        val (body,_) = entity[Physic]
        var (maxHeight , lowerFeet,upperFeet,jump,doubleJump,wallJumpFruitTimer,doubleJumpFruitTimer,jumpCounter) = entity[Jump]

        if (wallJumpFruitTimer > 0f){
            wallJumpFruitTimer = (wallJumpFruitTimer - deltaTime).coerceAtLeast(0f)
            jumpComps.wallJumpFruitTimer = wallJumpFruitTimer
            if (wallJumpFruitTimer == 0f){
                GameEventDispatcher.fireEvent(EndFruitEffectEvent(FruitDrawable.MELON,0))
            }
        }

        if (doubleJump){
            applyJumpForce(jumpComps,body,maxHeight * 0.85f)
            jumpComps.doubleJump = false
            entity[State].stateMachine.changeState(PlayerState.DOUBLE_JUMP)
            return
        }

        if (doubleJumpFruitTimer > 0f ){
            if (doubleJumpFruitTimer == 4f) fireFruitEventOnce = true
            doubleJumpFruitTimer = (doubleJumpFruitTimer - deltaTime).coerceAtLeast(0f)
            jumpComps.doubleJumpFruitTimer = doubleJumpFruitTimer
            if (jumpCounter <2 && jump){
                println("hello")
                jumpCounter += 1
                if (jumpCounter == 1)  applyJumpForce(jumpComps,body,maxHeight)
                if (jumpCounter == 2)  {
                    applyJumpForce(jumpComps,body,maxHeight* 0.85f)
                    entity[State].stateMachine.changeState(PlayerState.DOUBLE_JUMP)
                }
                audioService.play(SoundAsset.JUMP)
                jumpComps.doubleJumpCounter = jumpCounter
            }
            if (fireFruitEventOnce && doubleJumpFruitTimer < 1.25f) {
                GameEventDispatcher.fireEvent(EndFruitEffectEvent(FruitDrawable.APPLE,0))
                fireFruitEventOnce = false
            }
        }

        if (entity has Fly) return
        if (!jump) return

        if (!MathUtils.isEqual(body.linearVelocity.y,0f,3.5f)){
            return
        }

        val lowerX = body.position.x + lowerFeet.x - 0.1f
        val lowerY = body.position.y + lowerFeet.y - 0.1f
        val upperX = body.position.x + upperFeet.x + 0.1f
        val upperY = body.position.y + upperFeet.y + 0.1f

        DEBUG_RECT.set(lowerX,lowerY,upperX-lowerX,upperY-lowerY)
        physicWorld.query(lowerX,lowerY,upperX,upperY){fixture ->
            val categoryBit = fixture.filterData.categoryBits
            val userData = fixture.userData
            if (fixture.filterData.categoryBits == PLAYER_BIT) return@query true
            if ((categoryBit == GROUND_BIT && userData == "canJump") ||
                ((categoryBit == ROCK_HEAD_BIT || categoryBit == SPIKE_HEAD_BIT ) && userData == "hitbox" && entity hasNo Invulnarable) ||
                ((categoryBit == PLATFORM_BIT || wallJumpFruitTimer > 0f) && (!fixture.isSensor && fixture.userData != "mapBoundary")) ){
                applyJumpForce(jumpComps,body,maxHeight)
                audioService.play(SoundAsset.JUMP)
                return@query false
            }
            return@query true
        }
    }

    private fun applyJumpForce(jumpComp : Jump,body: Body,maxHeight:Float ){
        val gravityY = if (physicWorld.gravity.y == 0f) 1f else physicWorld.gravity.y
        body.setLinearVelocity(body.linearVelocity.x, sqrt(2 * maxHeight * -gravityY))
        jumpComp.jump = false
    }


}
