package com.fatih.pixeladventure.util

enum class GameObject {
    FROG,CHAINSAW;
    val atlasKey = this.name.lowercase()
}
