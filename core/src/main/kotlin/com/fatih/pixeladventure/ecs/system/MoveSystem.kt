package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.Interpolation
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.ecs.component.Track
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class MoveSystem : IteratingSystem(family = family{all(Move).none(Track)}) {

    override fun onTickEntity(entity: Entity) {
        val moveComp = entity[Move]
        var (isFlipX,direction,current,max,timer,timeToMax) = moveComp
        if (direction != MoveDirection.NONE){
            if ((current >0 && direction == MoveDirection.LEFT )|| (current<0 && direction == MoveDirection.RIGHT)){
                timer = 0f
            }
            timer = (timer + (deltaTime* (1f/timeToMax))).coerceAtMost(1f)
            current = pow50outInterpolation.apply(0f,max,timer)
            current *= direction.value
            isFlipX = direction == MoveDirection.LEFT
        }else{
            current = 0f
            timer = 0f
        }
        moveComp.current = current
        moveComp.timer = timer
        moveComp.flipX = isFlipX
    }

    companion object{
        val pow50outInterpolation: Interpolation.PowOut = Interpolation.pow5Out
    }
}
