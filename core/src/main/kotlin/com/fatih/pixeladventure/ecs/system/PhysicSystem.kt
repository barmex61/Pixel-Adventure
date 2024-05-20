package com.fatih.pixeladventure.ecs.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.fatih.pixeladventure.ai.FallingPlatformState
import com.fatih.pixeladventure.ai.FireTrapState
import com.fatih.pixeladventure.ai.FlagState
import com.fatih.pixeladventure.ai.TrambolineState
import com.fatih.pixeladventure.ecs.component.Aggro
import com.fatih.pixeladventure.ecs.component.Damage
import com.fatih.pixeladventure.ecs.component.DamageTaken
import com.fatih.pixeladventure.ecs.component.EntityTag
import com.fatih.pixeladventure.ecs.component.Fan
import com.fatih.pixeladventure.ecs.component.Graphic
import com.fatih.pixeladventure.ecs.component.Jump
import com.fatih.pixeladventure.ecs.component.Life
import com.fatih.pixeladventure.ecs.component.Move
import com.fatih.pixeladventure.ecs.component.Physic
import com.fatih.pixeladventure.ecs.component.State
import com.fatih.pixeladventure.ecs.component.Track
import com.fatih.pixeladventure.event.CollectItemEvent
import com.fatih.pixeladventure.event.GameEventDispatcher
import com.fatih.pixeladventure.event.PlayerOutOfMapEvent
import com.fatih.pixeladventure.event.VictoryEvent
import com.fatih.pixeladventure.game.PhysicWorld
import com.fatih.pixeladventure.util.PLATFORM_BIT
import com.fatih.pixeladventure.util.PLAYER_BIT
import com.fatih.pixeladventure.util.SoundAsset
import com.fatih.pixeladventure.util.TRAP_BIT
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
        val physicComp = entity[Physic]
        val (body,previousPosition) = physicComp

        previousPosition.set(body.position)
        entity.getOrNull(Move)?.let {moveComp ->
            val trackComp = entity.getOrNull(Track)
            if (trackComp != null) {
                body.setLinearVelocity(trackComp.moveX,trackComp.moveY)
                return
            }
            val aggroComp = entity.getOrNull(Aggro)
            if (aggroComp != null){
                when {
                    moveComp.direction.isNone() ->  body.setLinearVelocity(moveComp.current , moveComp.current)
                    moveComp.direction.isUpOrDown() -> body.setLinearVelocity(0f,moveComp.current)
                    moveComp.direction.isRightOrLeftOrNone() -> body.setLinearVelocity(moveComp.current,0f)
                }
                return
            }
            body.setLinearVelocity(moveComp.current , body.linearVelocity.y)
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

    private fun Fixture.isFanSensor() = this.userData == "fan_sensor"
    private fun Fixture.isHitBox() = this.userData == "hitbox"
    private fun Fixture.isCherry() = this.userData == "cherry"
    private fun Fixture.isPlatform() = this.filterData.categoryBits == PLATFORM_BIT
    private fun Fixture.isFallingPlatform() = this.userData == "falling_platform"
    private fun Fixture.isTramboline() = this.userData == "tramboline"
    private fun Fixture.isFanPlatform() = this.userData == "fan_platform"
    private fun Fixture.isPlayer() = this.filterData.categoryBits == PLAYER_BIT
    private fun Fixture.isAggro(entity: Entity,isBeginContact : Boolean) : Boolean {
        return when(this.userData){
            "horizontalAggroSensor" -> {
               entity[Aggro].horizontalCollision = isBeginContact
               true
            }
            "verticalAggroSensor" ->{
                entity[Aggro].verticalCollision = isBeginContact
                true
            }
            else -> false
        }
    }
    private fun Fixture.isBottomMapBoundary() = this.userData == "bottomMapBoundary"
    private fun Fixture.isPlayerFoot() = this.userData == "footFixture"

    private fun isDamageCollision(entityA: Entity,entityB: Entity,fixtureA: Fixture,fixtureB:Fixture) : Boolean{
        return  entityA has Damage && entityB has Life && fixtureB.isSensor && fixtureA.isHitBox() && fixtureB.isHitBox()
    }

    private fun handleDamageBeginContact(damageSource: Entity, damageTarget: Entity) = with(world){
        val (damageAmount) = damageSource[Damage]
        damageTarget.configure {
            val damageTakenComp = it.getOrAdd(DamageTaken){ DamageTaken(0) }
            damageTakenComp.damageAmount += damageAmount
        }
    }

    private fun handleDamageEndContact(damageSource: Entity, damageTarget: Entity) = with(world){
        damageTarget.getOrNull(DamageTaken)?.let {
            val (damageAmount) = damageSource[Damage]
            it.damageAmount -= damageAmount
            if (it.damageAmount <= 0){
                damageTarget.configure { it -= DamageTaken }
            }
        }
    }

    private fun isAggroSensorCollision(entityA:Entity,fixtureA : Fixture,fixtureB: Fixture,isBeginContact: Boolean): Boolean{
        return entityA has Aggro && fixtureA.isAggro(entityA,isBeginContact) && fixtureB.isHitBox()
    }

    private fun handleAggroBeginContact(aggroEntity: Entity,triggerEntity: Entity){
        aggroEntity[Aggro].aggroEntities += triggerEntity
    }

    private fun handleAggroEndContact(aggroEntity: Entity,triggerEntity: Entity){
        if (!aggroEntity[Aggro].horizontalCollision && ! aggroEntity[Aggro].verticalCollision){
            aggroEntity[Aggro].aggroEntities -= triggerEntity
            aggroEntity[Aggro].targetEntity = Entity.NONE
        }
    }

    private fun isPlayerBottomMapBoundaryCollision(entity: Entity?,fixtureB: Fixture): Boolean{
        if (entity == null) return false
        return entity has EntityTag.PLAYER && fixtureB.isBottomMapBoundary()
    }

    private fun handlePlayerOutOfMap(playerEntity: Entity) = with(world){
        GameEventDispatcher.fireEvent(PlayerOutOfMapEvent(playerEntity))
    }

    private fun isCollectableCollision(entityA: Entity,entityB: Entity,fixtureA: Fixture,fixtureB: Fixture) : Boolean{
        return entityA has EntityTag.PLAYER && entityB has EntityTag.COLLECTABLE && ((fixtureA.isPlayerFoot() && fixtureB.isCherry()) || (!fixtureB.isCherry() && fixtureA.isHitBox() ))
    }

    private fun handleCollectableBeginContact(playerEntity : Entity,collectableEntity : Entity) {
        GameEventDispatcher.fireEvent(CollectItemEvent(playerEntity,collectableEntity))
    }

    private fun isFinishFlagCollision(entityA: Entity,fixtureA: Fixture,fixtureB: Fixture): Boolean {
        return entityA has EntityTag.PLAYER && fixtureA.isHitBox() && fixtureB.userData == "finish_flag"
    }

    private fun handleFinishFlagCollision(playerEntity: Entity,flagEntity: Entity,flagFixture: Fixture) = with(world){

        playerEntity.configure {
            it -= EntityTag.PLAYER
            it -= Life
            val body = it[Physic].body
            body.setLinearVelocity(it[Move].max * it[Move].direction.valueX * -1,body.linearVelocity.y)
            body.linearDamping = 2f
            it -= Move
            GameEventDispatcher.fireEvent(VictoryEvent(SoundAsset.FLAG))
        }
        flagEntity[State].stateMachine.changeState(FlagState.RUN)
        flagFixture.userData = ""
    }

    private fun isGroundAndFootCollision(fixtureA: Fixture,fixtureB: Fixture) : Boolean {
        return fixtureA.isPlayerFoot() && fixtureB.filterData.categoryBits == PLATFORM_BIT
    }

    private fun handleGroundAndFootCollision(playerEntity: Entity){
        playerEntity[Jump].jumpCounter = 0
    }

    private fun isPlayerAndFallingPlatformCollision(fixtureA: Fixture,fixtureB: Fixture): Boolean{
        val isCollisionSuccess = fixtureA.isPlayer() && fixtureB.isPlatform() && fixtureB.isFallingPlatform()
        if (isCollisionSuccess) fixtureB.userData = "off"
        return isCollisionSuccess
    }

    private fun handlePlayerAndFallingPlatformCollision(platformEntity: Entity){
        platformEntity[State].stateMachine.changeState(FallingPlatformState.OFF)
    }

    private fun isPlayerAndFanCollision(fixtureA: Fixture,fixtureB: Fixture,isBeginContact: Boolean = false) : Boolean {
        return fixtureA.isPlayer() && (fixtureB.isFanSensor() || if (isBeginContact) fixtureB.isFanPlatform() else false)
    }

    private fun handlePlayerAndFanBeginCollision(playerEntity: Entity, fanEntity: Entity) = with(world){
        fanEntity[Fan].collideEntity = playerEntity
    }

    private fun handlePlayerAndFanEndCollision(fanEntity: Entity) = with(world){
        fanEntity[Fan].collideEntity = null
    }

    private fun isPlayerAndTrambolineCollision(fixtureA: Fixture,fixtureB: Fixture) : Boolean{
        return fixtureA.isPlayerFoot() && fixtureB.isTramboline()
    }

    private fun handlePlayerAndTrambolineCollision(playerEntity: Entity,trambolineEntity : Entity){
        val playerBody = playerEntity[Physic].body
        playerBody.setLinearVelocity(playerBody.linearVelocity.x, 20f)
        trambolineEntity[State].stateMachine.changeState(TrambolineState.ON)
    }

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = contact.entityA
        val entityB = contact.entityB

        if (entityA == null || entityB == null){
            when{
                isPlayerBottomMapBoundaryCollision(entityA,fixtureB) -> handlePlayerOutOfMap(entityA!!)
                isPlayerBottomMapBoundaryCollision(entityB,fixtureA) -> handlePlayerOutOfMap(entityB!!)
                isGroundAndFootCollision(fixtureA,fixtureB) && entityA != null -> handleGroundAndFootCollision(entityA)
                isGroundAndFootCollision(fixtureB,fixtureA) && entityB != null-> handleGroundAndFootCollision(entityB)
            }
            return
        }
        when {
            isPlayerAndFanCollision(fixtureA,fixtureB,true) -> handlePlayerAndFanBeginCollision(entityA,entityB)
            isPlayerAndFanCollision(fixtureB,fixtureA,true) -> handlePlayerAndFanBeginCollision(entityB,entityA)
            isPlayerAndFallingPlatformCollision(fixtureA,fixtureB) -> handlePlayerAndFallingPlatformCollision(entityB)
            isPlayerAndFallingPlatformCollision(fixtureB,fixtureA) -> handlePlayerAndFallingPlatformCollision(entityA)
            isPlayerAndTrambolineCollision(fixtureA,fixtureB) -> handlePlayerAndTrambolineCollision(entityA,entityB)
            isPlayerAndTrambolineCollision(fixtureB,fixtureA) -> handlePlayerAndTrambolineCollision(entityB,entityA)
            isDamageCollision(entityA,entityB,fixtureA,fixtureB) -> handleDamageBeginContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureB,fixtureA) -> handleDamageBeginContact(entityB,entityA)
            isAggroSensorCollision(entityA,fixtureA,fixtureB,true) ->  handleAggroBeginContact(entityA,entityB)
            isAggroSensorCollision(entityB,fixtureB,fixtureA,true) -> handleAggroBeginContact(entityB,entityA)
            isCollectableCollision(entityA,entityB,fixtureA,fixtureB) -> handleCollectableBeginContact(entityA,entityB)
            isCollectableCollision(entityB,entityA,fixtureB,fixtureA) -> handleCollectableBeginContact(entityB,entityA)
            isFinishFlagCollision(entityA,fixtureA,fixtureB) -> handleFinishFlagCollision(entityA,entityB,fixtureB)
            isFinishFlagCollision(entityB,fixtureB,fixtureA) -> handleFinishFlagCollision(entityB,entityA,fixtureA)

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
        when {
            isPlayerAndFanCollision(fixtureA,fixtureB) -> handlePlayerAndFanEndCollision(entityB)
            isPlayerAndFanCollision(fixtureB,fixtureA) -> handlePlayerAndFanEndCollision(entityA)
            isDamageCollision(entityA,entityB,fixtureA,fixtureB) ->  handleDamageEndContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureB,fixtureA) -> handleDamageEndContact(entityB,entityA)
            isAggroSensorCollision(entityA,fixtureA,fixtureB,false) ->  handleAggroEndContact(entityA,entityB)
            isAggroSensorCollision(entityB,fixtureB,fixtureA,false) -> handleAggroEndContact(entityB,entityA)
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
