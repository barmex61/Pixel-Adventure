package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.fatih.pixeladventure.ecs.component.Damage
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.Interval
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.math.component1
import ktx.math.component2

class PhysicSystem(
    private val physicWorld: PhysicWorld = inject(),
    interval : Interval = Fixed(1/300f)
) : IteratingSystem(family = family {all(Physic,Graphic)}, interval = interval) , ContactListener {

    init {
        physicWorld.setContactListener(this)
    }

    override fun onUpdate() {
        if (physicWorld.autoClearForces){
            physicWorld.autoClearForces = false
        }
        super.onUpdate()
        physicWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicWorld.step(deltaTime,6,2)
    }

    override fun onTickEntity(entity: Entity) {
        val (body,previousPosition) = entity[Physic]
        previousPosition.set(body.position)
        entity.getOrNull(Move)?.let {moveComp ->
            val trackComp = entity.getOrNull(Track)
            if (trackComp == null){
                body.setLinearVelocity(moveComp.current , body.linearVelocity.y)
            }else{
                body.setLinearVelocity(trackComp.moveX,trackComp.moveY)
            }
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val (sprite) = entity[Graphic]
        val (body,previousPosition) = entity[Physic]
        val (prevX,prevY) = previousPosition
        val (bodyX,bodyY) = body.position
        sprite.setPosition(
            MathUtils.lerp(prevX,bodyX,alpha),
            MathUtils.lerp(prevY,bodyY,alpha)
        )
    }

    private val Fixture.entity : Entity?
        get() {
            val userData = this.body.userData
            return if (userData is Entity){
                userData
            }else null
        }

    private val Contact.entityA : Entity?
        get() = fixtureA.entity

    private val Contact.entityB : Entity?
        get() = fixtureB.entity

    private fun Fixture.isHitBox() = this.userData == "hitbox"

    private fun isDamageCollision(entityA: Entity,entityB: Entity,fixtureA: Fixture,fixtureB:Fixture) : Boolean{
        return entityA has Damage && entityB has Life && fixtureA.isHitBox() && fixtureB.isHitBox()
    }

    private fun handleDamageBeginContact(damageSource: Entity, damageTarget: Entity) = with(world){
        val (damageAmount) = damageSource[Damage]
        damageTarget.configure {
            val damageTakenComp = it.getOrAdd(DamageTaken){ DamageTaken(0) }
            damageTakenComp.amount += damageAmount
        }
    }

    private fun handleDamageEndContact(damageSource: Entity, damageTarget: Entity) = with(world){
        damageTarget.getOrNull(DamageTaken)?.let {
            val (damageAmount) = damageSource[Damage]
            it.amount -= damageAmount
            if (it.amount <= 0){
                damageTarget.configure { it -= DamageTaken }
            }
        }
    }

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = contact.entityA
        val entityB = contact.entityB
        if (entityA == null || entityB == null){
            return
        }
        if (isDamageCollision(entityA,entityB,fixtureA,fixtureB)){
            handleDamageBeginContact(entityA,entityB)
        }else if (isDamageCollision(entityB,entityA,fixtureB,fixtureA)){
            handleDamageBeginContact(entityB,entityA)
        }
    }



    override fun endContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = contact.entityA
        val entityB = contact.entityB
        if (entityA == null || entityB == null){
            return
        }
        if (isDamageCollision(entityA,entityB,fixtureA,fixtureB)){
            handleDamageEndContact(entityA,entityB)
        }else if (isDamageCollision(entityB,entityA,fixtureB,fixtureA)){
            handleDamageEndContact(entityB,entityA)
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
        if (contact.fixtureA.filterData.categoryBits == PLATFORM_BIT && contact.entityB != null){
            contact.isEnabled = contact.entityB!![Physic].body.linearVelocity.y <= 0.0001f
        }
        if (contact.fixtureB.filterData.categoryBits == PLATFORM_BIT && contact.entityA != null){
            contact.isEnabled = contact.entityA!![Physic].body.linearVelocity.y <= 0.0001f
        }
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit
}
