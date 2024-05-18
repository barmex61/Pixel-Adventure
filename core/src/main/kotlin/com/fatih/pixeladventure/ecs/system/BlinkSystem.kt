package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.ecs.component.Blink
import com.fatih.pixeladventure.ecs.component.Graphic
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import kotlin.math.max

class BlinkSystem : IteratingSystem(family = World.family { all(Blink,Graphic) }) {

    override fun onTickEntity(entity: Entity) {
        val blinkComp = entity[Blink]
        val (sprite,_) = entity[Graphic]
        val(maxTime,blinksRatio,_) = blinkComp
        if (maxTime <= 0f){
            sprite.setAlpha(1f)
            entity.configure { it -= Blink }
            return
        }
        blinkComp.maxTime -= deltaTime
        blinkComp.timer += deltaTime
        if (blinkComp.timer >= blinksRatio){
            blinkComp.timer = 0f
            sprite.setAlpha(if (sprite.color.a == 0f) 1f else 0f)
        }

    }
}
