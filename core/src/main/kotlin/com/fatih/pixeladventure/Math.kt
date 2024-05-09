package com.fatih.pixeladventure

import com.badlogic.gdx.math.Rectangle

operator fun Rectangle.component1() = this.x
operator fun Rectangle.component2() = this.y
operator fun Rectangle.component3() = this.width
operator fun Rectangle.component4() = this.height
