package com.fatih.pixeladventure.ai

import com.badlogic.gdx.Gdx
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.Damage
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Physic
import ktx.box2d.query

enum class FireTrapState : EntityState {
    OFF{
        override fun enter(entity: AiEntity) {

            entity.frameDuration = 0.1f
            entity.animation(AnimationType.OFF)
        }

        override fun update(entity: AiEntity) {
            entity.frameDuration -= Gdx.graphics.deltaTime
            if (entity.frameDuration <= 0f){
                entity.state(ON)
            }
        }
    },
    ON{
        override fun enter(entity: AiEntity) {
            entity.frameDuration = 3.5f
            entity.animation(AnimationType.ON)
        }

        override fun update(entity: AiEntity) {
            entity.frameDuration -= Gdx.graphics.deltaTime
            if (entity.frameDuration <= 0f) entity.state(OFF)
        }

    },

}
