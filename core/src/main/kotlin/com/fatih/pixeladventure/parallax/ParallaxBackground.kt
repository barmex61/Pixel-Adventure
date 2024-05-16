package com.fatih.pixeladventure.parallax

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.game.PixelAdventure
import com.fatih.pixeladventure.game.PixelAdventure.Companion.UNIT_SCALE
import ktx.assets.disposeSafely
import ktx.math.vec2

class ParallaxBackground(val viewPort: Viewport,val scrollSpeed : Vector2 = vec2(1f,1f)) : Disposable{

    private val originUV = vec2(0f,0f)
    private val originUV2 = vec2(0f,0f)

    private val texture = Texture("graphics/gray.png").apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }

    private val sprite = Sprite(texture).apply {
       resize(viewPort.worldWidth,viewPort.worldHeight)
    }

    private fun Sprite.resize(worldWidth : Float,worldHeight : Float){
        println("resize")
        setSize(worldWidth,worldHeight)
        originUV.set(u,v)
        originUV2.set(worldWidth / (texture.width * UNIT_SCALE), worldHeight / (texture.height * UNIT_SCALE))
        u2 = originUV2.x
        v2 = originUV2.y
    }

    fun scrollBy(amountX : Float, amountY : Float){
        sprite.u += amountX * scrollSpeed.x
        sprite.u2 += amountX * scrollSpeed.x
        sprite.v += amountY * scrollSpeed.y
        sprite.v2 += amountY * scrollSpeed.y
    }

    fun scrollTo(scrollX : Float,scrollY: Float) = with(sprite){
        sprite.u = originUV.x + scrollX * scrollSpeed.x
        sprite.v = originUV.y + scrollY * scrollSpeed.y
        sprite.u2 = originUV2.x + scrollX * scrollSpeed.x
        sprite.v2 = originUV2.y + scrollY * scrollSpeed.y
    }

    fun draw(x: Float,y : Float,batch: Batch){
        sprite.setPosition(x,y)
        sprite.draw(batch)
    }

    fun resize(worldWidth: Float,worldHeight: Float){
        sprite.resize(worldWidth,worldHeight)
    }

    override fun dispose() {
        texture.disposeSafely()
    }
}
