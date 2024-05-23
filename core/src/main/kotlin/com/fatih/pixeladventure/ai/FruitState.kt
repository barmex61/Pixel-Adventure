package com.fatih.pixeladventure.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.EntityTag


enum class FruitState : EntityState{
    IDLE{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.IDLE,PlayMode.LOOP)
        }

        override fun update(entity: AiEntity) {

        }
    },


    HIT_RESPAWN {

        override fun enter(entity: AiEntity) {
            entity.fruitRespawnDuration = 2f
            entity.animation(AnimationType.HIT, PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            entity.fruitRespawnDuration -= entity.world.deltaTime
            if (entity.isAnimationDone() && entity.fruitRespawnDuration <= 0f) entity.state(RESPAWN)
        }
    },

    HIT_DESTROY {

        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.HIT, PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone() ) entity.destroy()
        }
    },

    RESPAWN{
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.HIT, PlayMode.REVERSED)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone() ) {
                with(entity.world){
                    entity.entity.configure {
                        it += EntityTag.COLLECTABLE
                    }
                    entity.state(IDLE)
                }
            }
        }

    }
}
