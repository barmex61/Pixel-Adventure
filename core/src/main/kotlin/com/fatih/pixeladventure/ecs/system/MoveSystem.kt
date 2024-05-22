package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.Interpolation
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.MoveDirection
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.EndBananaEffectEvent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family

class MoveSystem : IteratingSystem(family = family{all(Move).none(Track)}) {

    override fun onTickEntity(entity: Entity) {
        val moveComp = entity[Move]
        var (isFlipX,direction,current,max,timer,timeToMax,_,defaultMax,maxReduceTimer) = moveComp

        //max speed reducer
        if (max > defaultMax ) {
            maxReduceTimer += deltaTime
            if (maxReduceTimer >= 0.5f){
                moveComp.max -= 0.5f
                maxReduceTimer = 0f
                GameEventDispatcher.fireEvent(EndBananaEffectEvent((max - defaultMax).toInt()))
            }
            moveComp.maxReduceTimer = maxReduceTimer
            println(max)
        }

        if (direction != MoveDirection.NONE){
            moveComp.previousDirection = direction
            if ((current >0 && direction.isLeftOrDown() )|| (current<0 && direction.isRightOrUp())){
                timer = 0f
            }
            timer = (timer + (deltaTime* (1f/timeToMax))).coerceAtMost(1f)
            current = pow50outInterpolation.apply(1.5f,max,timer)
            current *= direction.valueX
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
        val interpolation = Interpolation.linear
    }
}
