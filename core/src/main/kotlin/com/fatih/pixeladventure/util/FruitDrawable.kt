package com.fatih.pixeladventure.util

enum class FruitDrawable {
    BANANA,CHERRY,APPLE,MELON,PINEAPPLE,KIWI;
    val drawablePath = this.name.lowercase()
}
