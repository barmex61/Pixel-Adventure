package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport
import com.fatih.pixeladventure.ecs.component.Text
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.inject
import ktx.graphics.use
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin

class TextSystem (
    private val uiViewport: Viewport = inject("uiViewport"),
    private val gameViewport : Viewport = inject("gameViewport"),
    private val spriteBatch : SpriteBatch = inject()
): IteratingSystem(family = World.family { all(Text) }) {

    private val bitmapFont = Scene2DSkin.defaultSkin.getFont("default_fnt_small").apply {
        this.data.markupEnabled = true
    }
    private val textPosition = vec2()
    private val textTopRight = vec2()

    override fun onTickEntity(entity: Entity) {
        val textComp = entity[Text]
        val (txt,boundary) = textComp

        textPosition.set(boundary.x,boundary.y)
        textTopRight.set(boundary.x + boundary.width,boundary.y + boundary.height)

        gameViewport.project(textPosition)
        uiViewport.unproject(textPosition)

        gameViewport.project(textTopRight)
        uiViewport.unproject(textTopRight)

        spriteBatch.use(uiViewport.camera.combined) {
            bitmapFont.draw(it,txt,textPosition.x,uiViewport.worldHeight - textPosition.y + 40f,textTopRight.x - textPosition.x ,Align.center,true)
        }

    }
}
