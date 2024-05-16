package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.Vector2
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Physic
import com.github.quillraven.fleks.configureWorld
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.createWorld
import ktx.math.vec2
import kotlin.test.Test


class JumpSystemTest{
    private val physicWorld = createWorld(gravity = Vector2.Zero).apply { autoClearForces = false }
    private val world = configureWorld {
        injectables {
            add(physicWorld)
        }
        systems {
            add(JumpSystem())
            add(PhysicSystem())
        }
    }

    @Test
    fun testJumpSystem(){
        val entity = world.entity {
            it += Jump(maxHeight = 2f, buffer = 0.25f, lowerFeet = vec2(), upperFeet = vec2())
            val body = physicWorld.body {
                box(1f,1f)
                position.set(0f,0f)
            }
            it += Physic(body)
        }
        with(world){
            world.update(0.1f)
            println(entity[Physic].body.position.y)
        }

    }

}
