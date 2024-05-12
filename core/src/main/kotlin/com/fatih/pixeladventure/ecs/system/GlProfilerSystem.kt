package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.github.quillraven.fleks.IntervalSystem

class GlProfilerSystem : IntervalSystem() {

    private val glProfiler = GLProfiler(Gdx.graphics).apply { enable() }

    override fun onTick() {
        Gdx.graphics.setTitle("textureBindings ${glProfiler.textureBindings}, drawCalls ${glProfiler.drawCalls}, calls ${glProfiler.calls} fps ${Gdx.app.graphics.framesPerSecond}")
        glProfiler.reset()
    }


}
