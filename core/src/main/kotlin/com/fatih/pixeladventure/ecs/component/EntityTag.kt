package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class EntityTag : EntityTags by entityTagOf() {
    PLAYER,
}