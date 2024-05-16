package com.fatih.pixeladventure.util

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.fatih.pixeladventure.ecs.component.AnimationType
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.system.AnimationSystem
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

fun World.animation(entity: Entity,animationType: AnimationType,playMode : PlayMode = PlayMode.LOOP) {
    if (entity has EntityTag.PLAYER) println(animationType)
    val animationSystem = this.system<AnimationSystem>()
    animationSystem.entityAnimation(entity,animationType,playMode)
}
