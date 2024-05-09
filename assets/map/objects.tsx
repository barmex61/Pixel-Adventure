<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="objects" tilewidth="32" tileheight="32" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <properties>
   <property name="GameObject" value="FROG"/>
  </properties>
  <image width="32" height="32" source="../graphics/frog.png"/>
  <objectgroup draworder="index" id="2">
   <object id="3" x="8" y="11" width="17" height="11"/>
   <object id="4" x="8" y="15" width="17" height="17">
    <ellipse/>
   </object>
   <object id="15" x="2" y="7">
    <polygon points="0,0 -2,6 1,10"/>
   </object>
   <object id="16" x="29" y="6">
    <polygon points="0,0 -2,11 2,6"/>
   </object>
   <object id="18" x="8" y="3" width="17" height="6">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
</tileset>
