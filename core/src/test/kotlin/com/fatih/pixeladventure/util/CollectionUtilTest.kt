package com.fatih.pixeladventure.util

import com.badlogic.gdx.utils.ObjectMap
import ktx.collections.set
import kotlin.test.Test
import kotlin.test.assertEquals

class CollectionUtilTest {

    @Test
    fun testGetOrDefaultProperty(){
        val testMap = ObjectMap<String,String>().apply {
            set("intKey1","1")
            set("intKey2","2")
            set("intKey3","3")
            set("intKey4","4")
            set("booleanKey1","true")
            set("booleanKey2","false")
            set("strKey","string")
        }
        assertEquals(1,testMap.getOrDefault("intKey1",1))
        assertEquals(2,testMap.getOrDefault("intKey2",2))
        assertEquals(3,testMap.getOrDefault("intKey3",3))
        assertEquals(4,testMap.getOrDefault("intKey4",4))
        assertEquals(true,testMap.getOrDefault("booleanKey1",true))
        assertEquals(false,testMap.getOrDefault("booleanKey2",false))
        assertEquals("string",testMap.getOrDefault("strKey","string"))
        assertEquals("defaultValue",testMap.getOrDefault("invalidKey","defaultValue"))
    }
}
