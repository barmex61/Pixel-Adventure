<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="32" tileheight="32" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="GameObject" propertytype="GameObject" value="FROG"/>
  </properties>
  <image width="32" height="32" source="../graphics/frog.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="9" y="11" width="15" height="21">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="hitbox"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="9" y="16">
    <polyline points="0,0 0,11"/>
   </object>
   <object id="6" type="FixtureDef" x="24" y="16">
    <polyline points="0,0 0,11"/>
   </object>
   <object id="13" x="9" y="30">
    <polygon points="0,-1 3,2 13,2 15,-1"/>
   </object>
   <object id="18" type="FixtureDef" x="9" y="16">
    <properties>
     <property name="restitution" type="float" value="1"/>
    </properties>
    <polygon points="0,0 5,-5 11,-5 15,0"/>
   </object>
  </objectgroup>
 </tile>
</tileset>
