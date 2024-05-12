<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="38" tileheight="38" tilecount="2" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.065"/>
   <property name="entityTags" propertytype="EntityTag" value="PLAYER,CAMERA_FOCUS"/>
   <property name="gameObject" propertytype="GameObject" value="FROG"/>
   <property name="gravityScale" type="float" value="1"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="hasState" type="bool" value="true"/>
   <property name="jumpHeight" type="float" value="2.2"/>
   <property name="life" type="int" value="4"/>
   <property name="speed" type="float" value="7"/>
   <property name="startAnimType" propertytype="AnimType" value="IDLE"/>
   <property name="timeToMax" type="float" value="2.5"/>
  </properties>
  <image width="32" height="32" source="../graphics/frog.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="9" y="11" width="15" height="20">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="12" y="18">
    <polyline points="0,0 0,5.5 0,11"/>
   </object>
   <object id="6" type="FixtureDef" x="24" y="18">
    <polyline points="-2,0 -2,11"/>
   </object>
   <object id="13" type="FixtureDef" x="9" y="30">
    <polygon points="3,-1 3,2 13,2 13,-1"/>
   </object>
   <object id="18" type="FixtureDef" x="12" y="18">
    <properties>
     <property name="restitution" type="float" value="1"/>
    </properties>
    <polygon points="0,0 4,-5 6,-5 10,0"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="1" type="EntityDef">
  <properties>
   <property name="animFrameDuration" type="float" value="0.06"/>
   <property name="entityTags" propertytype="EntityTag" value="FOLLOW_TRACK"/>
   <property name="gameObject" propertytype="GameObject" value="CHAINSAW"/>
   <property name="gravityScale" type="float" value="0"/>
   <property name="hasAnimation" type="bool" value="true"/>
   <property name="speed" type="float" value="7"/>
   <property name="startAnimType" propertytype="AnimType" value="ON"/>
   <property name="timeToMax" type="float" value="0.1"/>
  </properties>
  <image width="38" height="38" source="../graphics/Off.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="2" y="2" width="34" height="34">
    <properties>
     <property name="density" type="float" value="30"/>
     <property name="isChain" type="bool" value="true"/>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
