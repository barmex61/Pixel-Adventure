package com.fatih.pixeladventure.ecs.system

import com.fatih.pixeladventure.audio.AudioService
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Invulnarable
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.event.EntityLifeChangeEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.util.SoundAsset
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World

class DamageSystem(
    private val audioService: AudioService = World.inject()
) : IteratingSystem(family = World.family { all(DamageTaken,Life).none(Invulnarable) }) {

    override fun onTickEntity(entity: Entity) {
        val (damageAmount) = entity[DamageTaken]
        val lifeComp = entity[Life]
        lifeComp.current = (lifeComp.current - damageAmount).coerceAtLeast(0)
        audioService.play(SoundAsset.HURT)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(entity))
        if (entity has EntityTag.PLAYER){
            entity.configure { it += Invulnarable(1f) }
        }
    }

}
