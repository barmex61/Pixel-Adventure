<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="64" tileheight="64" tilecount="10" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.065"/>
   <property name="bodyType" propertytype="BodyType" value="DynamicBody"/>
   <property name="entityTags" propertytype="EntityTags" value="PLAYER,CAMERA_FOCUS"/>
   <property name="gameObject" propertytype="GameObject" value="FROG"/>
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
   <object id="19" type="FixtureDef" x="11" y="12" width="11" height="12">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
    </properties>
    <ellipse/>
   </object>
   <object id="21" type="FixtureDef" x="11" y="26">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
     <property name="userData" value="player_foot"/>
    </properties>
    <polygon points="0,0 0,6 11,6 11,0"/>
   </object>
   <object id="25" type="FixtureDef" x="11" y="26">
    <properties>
     <property name="friction" type="float" value="0"/>
     <property name="gameObject" propertytype="GameObject" value="PLAYER"/>
     <property name="restitution" type="float" value="0"/>
     <property name="userData" value="frictionFixture"/>
    </properties>
    <polygon points="0,0 11,0 11,-11 0,-11"/>
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
 <tile id="3" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="CHERRY"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/cherry.png"/>
  <objectgroup draworder="index" id="3">
   <object id="8" type="FixtureDef" x="9" y="15" width="14" height="7">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="CHERRY"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="cherry"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="4" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="FINISH_FLAG"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="64" height="64" source="../graphics/finish_flag.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="22" y="18" width="3" height="46">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="FINISH_FLAG"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="finish_flag"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="5" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="1"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value=""/>
   <property name="gameObject" propertytype="GameObject" value="START_FLAG"/>
   <property name="startAnimType" propertytype="AnimType" value="START"/>
  </properties>
  <image width="19" height="36" source="../graphics/start_flag.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="19" height="36">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="START_FLAG"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="start_flag"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="6" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="BANANA"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/banana.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="10" y="17">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="BANANA"/>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
    <polygon points="0,0 4,4 9,4 12,2 12,-1 10,-5 0,-5"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="7" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="MELON"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/melon.png"/>
  <objectgroup draworder="index" id="2">
   <object id="4" type="FixtureDef" x="8" y="11">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="MELON"/>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
    <polygon points="0,0 0,5 5,10 11,10 16,5 16,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="8" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="PINEAPPLE"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/pineapple.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="11" y="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PINEAPPLE"/>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
    <polygon points="0,0 0,7 1,8 9,8 10,7 10,0 8,-3 2,-3"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="9" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.035"/>
   <property name="bodyType" propertytype="BodyType" value="StaticBody"/>
   <property name="entityTags" propertytype="EntityTags" value="FOREGROUND,COLLECTABLE"/>
   <property name="gameObject" propertytype="GameObject" value="KIWI"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
  </properties>
  <image width="32" height="32" source="../graphics/kiwi.png"/>
  <objectgroup draworder="index" id="4">
   <object id="3" type="FixtureDef" x="9" y="9" width="14" height="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="KIWI"/>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
