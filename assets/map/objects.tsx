<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="42" tileheight="42" tilecount="3" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.065"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER,CAMERA_FOCUS"/>
   <property name="gameObject" propertytype="GameObject" value="FROG"/>
   <property name="gameObjectState" propertytype="GameObjectState" value="IDLE"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="jumpHeight" type="float" value="2.2"/>
   <property name="life" type="int" value="4"/>
   <property name="speed" type="float" value="7"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2.5"/>
  </properties>
  <image width="32" height="32" source="../graphics/frog.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="9" y="11" width="15" height="21">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="11" y="18">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
    </properties>
    <polyline points="0,0 0,5.5 0,11"/>
   </object>
   <object id="6" type="FixtureDef" x="24" y="18">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
    </properties>
    <polyline points="-2,0 -2,11"/>
   </object>
   <object id="19" type="FixtureDef" x="11" y="12" width="11" height="12">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
    </properties>
    <ellipse/>
   </object>
   <object id="21" type="FixtureDef" x="11" y="29">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
     <property name="userData" value="player_foot"/>
    </properties>
    <polygon points="0,0 0,3 11,3 11,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="1" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.06"/>
   <property name="bodyType" propertytype="BodyType" value="KinematicBody"/>
   <property name="damage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value="HAS_TRACK"/>
   <property name="gameObject" propertytype="GameObject" value="CHAINSAW"/>
   <property name="speed" type="float" value="7"/>
   <property name="startAnimType" propertytype="AnimType" value="ON"/>
   <property name="timeToMax" type="float" value="0.1"/>
  </properties>
  <image width="38" height="38" source="../graphics/chainsaw.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="5" y="5" width="28" height="28">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="CHAINSAW"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="2" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.1"/>
   <property name="bodyType" propertytype="BodyType" value="KinematicBody"/>
   <property name="damage" type="int" value="1"/>
   <property name="entityTags" propertytype="EntityTags" value="HAS_AGGRO"/>
   <property name="gameObject" propertytype="GameObject" value="ROCK_HEAD"/>
   <property name="gameObjectState" propertytype="GameObjectState" value="ROCK_HEAD_IDLE"/>
   <property name="speed" type="float" value="12"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="3"/>
  </properties>
  <image width="42" height="42" source="../graphics/rock_head.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="4" y="4" width="34" height="34">
    <properties>
     <property name="density" type="float" value="20"/>
     <property name="gameObject" propertytype="GameObject" value="ROCK_HEAD"/>
     <property name="isSensor" type="bool" value="false"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="8" y="-59" width="27" height="160">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="ROCK_HEAD"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="verticalAggroSensor"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="-59" y="8" width="160" height="26">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="ROCK_HEAD"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="horizontalAggroSensor"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
