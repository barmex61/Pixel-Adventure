package com.fatih.pixeladventure.util

import kotlin.experimental.or

const val CHAINSAW_BIT : Short = 1
const val PLAYER_BIT : Short = 2
const val GROUND_BIT : Short = 4
const val PLATFORM_BIT : Short = 8
const val ROCK_HEAD_BIT : Short = 16

enum class GameObject(val categoryBit : Short, val maskBits : Short) {
    CHAINSAW(CHAINSAW_BIT, PLAYER_BIT or GROUND_BIT ),
    PLAYER(PLAYER_BIT, CHAINSAW_BIT or GROUND_BIT or ROCK_HEAD_BIT),
    FROG(PLAYER_BIT, CHAINSAW_BIT or GROUND_BIT ),
    GROUND(GROUND_BIT, PLAYER_BIT or CHAINSAW_BIT or ROCK_HEAD_BIT),
    ROCK_HEAD(ROCK_HEAD_BIT, PLAYER_BIT or GROUND_BIT),
    PLATFORM(PLATFORM_BIT, PLAYER_BIT);
    val atlasKey = this.name.lowercase()
}
