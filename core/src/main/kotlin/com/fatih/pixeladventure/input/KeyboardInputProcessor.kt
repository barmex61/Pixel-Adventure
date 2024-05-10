package com.fatih.pixeladventure.input

import com.badlogic.gdx.Input
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Jump.Companion.JUMP_BUFFER_TIME
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter

class KeyboardInputProcessor(world: World) : KtxInputAdapter {

    private var moveX = 0
    private val playerEntities = world.family { all(EntityTag.PLAYER) }

    private fun updatePlayerMovement(moveValue : Int){
        moveX += moveValue
        playerEntities.forEach { it[Move].direction = MoveDirection.of(moveX) }
    }

    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(1)
            Input.Keys.A -> updatePlayerMovement(-1)
            Input.Keys.SPACE -> playerEntities.forEach { it[Jump].buffer = JUMP_BUFFER_TIME }
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
        }
        return false
    }
}
