package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.fatih.pixeladventure.game.PhysicWorld
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject

class GlProfilerSystem(
    private val physicWorld: PhysicWorld = inject()
) : IntervalSystem() {

    private val glProfiler = GLProfiler(Gdx.graphics).apply { enable() }

    override fun onTick() {
        Gdx.graphics.setTitle("textureBindings ${glProfiler.textureBindings}, drawCalls ${glProfiler.drawCalls}, calls ${glProfiler.calls} fps ${Gdx.app.graphics.framesPerSecond} entities ${world.numEntities} bodies ${physicWorld.bodyCount}")
        glProfiler.reset()
    }


}
