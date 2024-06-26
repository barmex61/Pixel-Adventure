<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.10.2" name="terrain-extruded" tilewidth="16" tileheight="16" spacing="8" margin="4" tilecount="242" columns="22">
 <image source="../graphics/terrain-ext.png" width="528" height="264"/>
 <tile id="2">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16"/>
  </objectgroup>
 </tile>
 <tile id="6">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="2" y="2" width="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="7">
  <objectgroup draworder="index" id="2">
   <object id="4" type="FixtureDef" x="0" y="2" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="8">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="2" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="12">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="GROUND"/>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="13">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="14">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="15">
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="4" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="17">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="1" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="18">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="1" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="19">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="24">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16"/>
  </objectgroup>
 </tile>
 <tile id="31">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="8" y="0" width="8" height="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="32">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="8" height="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="34">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="isSensor" type="bool" value="false"/>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="35">
  <objectgroup draworder="index" id="2">
   <object id="5" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="6" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="36">
  <objectgroup draworder="index" id="2">
   <object id="4" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="37">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="39">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="1" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="40">
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDef" x="0" y="1" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="41">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="46">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16"/>
  </objectgroup>
 </tile>
 <tile id="57">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="58">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="59">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="61">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="1" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
     <property name="userData" value=""/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="62">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="1" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="63">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="1" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="94">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="2" y="2" width="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
     <property name="restitution" type="float" value="0"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="95">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="2" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
     <property name="restitution" type="float" value="0"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="96">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="2" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="100">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="101">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="102">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="103">
  <objectgroup draworder="index" id="2">
   <object id="4" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="119">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="11" y="0" width="5" height="12">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="120">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="6" height="12">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="122">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="isSensor" type="bool" value="false"/>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="123">
  <objectgroup draworder="index" id="2">
   <object id="5" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="6" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="124">
  <objectgroup draworder="index" id="2">
   <object id="4" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="5" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="125">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="145">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="146">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="147">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="176">
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="7" height="16"/>
   <object id="2" x="0" y="0" width="16" height="6"/>
  </objectgroup>
 </tile>
 <tile id="177">
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="16" height="6"/>
  </objectgroup>
 </tile>
 <tile id="178">
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="16" height="6"/>
   <object id="2" x="9" y="0" width="7" height="16"/>
  </objectgroup>
 </tile>
 <tile id="179">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="11" y="11" width="5" height="5">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="180">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="11" width="4" height="5">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="182">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="2" y="2" width="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="183">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="2" width="16">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="184">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="2" width="15">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="188">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="189">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="190">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="191">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="193">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="194">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="195">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="196">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="198">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="7" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="200">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="9" y="0" width="7" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="201">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="12" y="0" width="4" height="5">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="202">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="5" height="5">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="207">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="8" y="0" width="8" height="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="208">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="8" height="14">
    <properties>
     <property name="gameObject" propertytype="GameObject" value="PLATFORM"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="210">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="3" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="isSensor" type="bool" value="false"/>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="211">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="212">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="213">
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="215">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="4" y="0" width="8" height="1">
    <properties>
     <property name="isSensor" type="bool" value="false"/>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="216">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="4" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="217">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
   <object id="2" type="FixtureDef" x="0" y="0" width="12" height="1">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="218">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="220">
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="7" height="16"/>
   <object id="2" type="FixtureDef" x="0" y="10" width="16" height="6">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="221">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="11" width="16" height="5">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="222">
  <objectgroup draworder="index" id="2">
   <object id="1" x="9" y="0" width="7" height="16"/>
   <object id="2" type="FixtureDef" x="0" y="10" width="16" height="6">
    <properties>
     <property name="userData" value="canJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="233">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="234">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="235">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="238">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="239">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="0"/>
     <property name="friction" type="float" value="0"/>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="240">
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDef" x="0" y="0" width="16" height="16">
    <properties>
     <property name="userData" value="cantJump"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
