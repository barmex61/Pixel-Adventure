package com.fatih.pixeladventure.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.TouchpadAlphaEvent
import com.github.quillraven.fleks.World
import ktx.actors.KtxInputListener
import ktx.app.KtxInputAdapter

class KeyboardInputProcessor(world: World) : KtxInputAdapter {

    private var moveX = 0
    private var playerEntities = with(world) {
        family { all(EntityTag.PLAYER) }
    }
    var stopMovement : Boolean = false

    fun resetMoveX() {
        updatePlayerMovement(0,true)
    }

    fun updatePlayerMovement(moveValue : Int,reset : Boolean = false){
        if (stopMovement) {
            stopMovement()
            return
        }
        if ((reset && moveX == 0) || moveX == moveValue) return
        moveX = (moveX + moveValue).coerceIn(-1,1)
        if (reset) moveX = 0
        playerEntities.forEach {
            it.getOrNull(Move)?.let {
                it.direction = MoveDirection.horizontalValueOf(moveX)
            }
        }
    }

    private fun stopMovement(){
        moveX = 0
        playerEntities.forEach {
            it.getOrNull(Move)?.let {
                it.direction = MoveDirection.horizontalValueOf(moveX)
            }
        }
    }

    fun updatePlayerJump(jump : Boolean){
        playerEntities.forEach { it[Jump].jump = jump}
    }

    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(1)
            Input.Keys.A -> updatePlayerMovement(-1)
            Input.Keys.SPACE -> updatePlayerJump(true)
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
            Input.Keys.SPACE -> updatePlayerJump(false)
        }
        return false
    }
}

