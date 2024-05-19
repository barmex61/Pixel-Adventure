package com.fatih.pixeladventure.input

import com.badlogic.gdx.Input
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter

class KeyboardInputProcessor(world: World) : KtxInputAdapter {

    private var moveX = 0
    private var playerEntities = with(world) {
        family { all(EntityTag.PLAYER) }
    }

    fun resetMoveX() {
        moveX = 0
    }

    private fun updatePlayerMovement(moveValue : Int){
        moveX += moveValue
        playerEntities.forEach { it[Move].direction = MoveDirection.horizontalValueOf(moveX) }
    }

    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(1)
            Input.Keys.A -> updatePlayerMovement(-1)
            Input.Keys.SPACE -> playerEntities.forEach { it[Jump].jump = true}
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
            Input.Keys.SPACE -> playerEntities.forEach { it[Jump].jump = false }
        }
        return false
    }
}
