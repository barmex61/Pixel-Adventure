package com.fatih.pixeladventure.ecs.component

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class EntityTag() : EntityTags by entityTagOf() {
    PLAYER,CAMERA_FOCUS,HAS_TRACK,HAS_AGGRO,BACKGROUND,FOREGROUND,COLLECTABLE,FAN,SOUND_EFFECT
}
