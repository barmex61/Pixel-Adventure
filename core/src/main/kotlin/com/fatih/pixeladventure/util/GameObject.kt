package com.fatih.pixeladventure.util

import kotlin.experimental.or

const val CHAINSAW_BIT : Short = 1
const val PLAYER_BIT : Short = 2
const val GROUND_BIT : Short = 4
const val PLATFORM_BIT : Short = 8
const val ROCK_HEAD_BIT : Short = 16
const val FRUIT_BIT : Short = 32
const val FLAG_BIT : Short = 64

enum class GameObject(val categoryBit : Short, val maskBits : Short) {
    CHAINSAW(CHAINSAW_BIT, PLAYER_BIT or GROUND_BIT ),
    PLAYER(PLAYER_BIT, CHAINSAW_BIT or GROUND_BIT or ROCK_HEAD_BIT or FRUIT_BIT or FLAG_BIT),
    FROG(PLAYER_BIT, CHAINSAW_BIT or GROUND_BIT ),
    GROUND(GROUND_BIT, PLAYER_BIT or CHAINSAW_BIT or ROCK_HEAD_BIT),
    ROCK_HEAD(ROCK_HEAD_BIT, PLAYER_BIT or GROUND_BIT),
    CHERRY(FRUIT_BIT, PLAYER_BIT),
    BANANA(FRUIT_BIT, PLAYER_BIT),
    KIWI(FRUIT_BIT, PLAYER_BIT),
    MELON(FRUIT_BIT, PLAYER_BIT),
    PINEAPPLE(FRUIT_BIT, PLAYER_BIT),
    START_FLAG(FLAG_BIT, PLAYER_BIT),
    FINISH_FLAG(FLAG_BIT, PLAYER_BIT),
    PLATFORM(PLATFORM_BIT, PLAYER_BIT);
    val atlasKey = if (this.name == "START_FLAG" || this.name == "FINISH_FLAG") "flag" else this.name.lowercase()
}
