package com.tailf.jnc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.tailf.jnc.YangException;
import com.tailf.jnc.YangInt16;
import com.tailf.jnc.YangType;

public class YangTypeTest {

    private YangType<Object> objType;
    private YangType<String> strType;
    private YangType<Short> shortType1;
    private YangType<Short> shortType2;
    private YangType<Long> longType;
    private YangInt32 i32;
    
    private class YangTypeDummy<T> extends YangType<T> {
        private static final long serialVersionUID = 1L;
        public YangTypeDummy() {}
        public YangTypeDummy(String s) throws YangException { super(s); }
        public YangTypeDummy(T t) throws YangException { super(t); }
        @Override protected T fromString(String s) { return null; }
        @Override public void check() throws YangException {}
        @Override public boolean canEqual(Object obj) { return true; }
    }

    @Before
    public void setUp() throws Exception {
        objType = new YangTypeDummy<Object>("7");
        strType = new YangTypeDummy<String>();
        shortType1 = new YangTypeDummy<Short>((short)7);
        shortType2 = new YangInt16("7");
        longType = new YangTypeDummy<Long>(7L);
        i32 = new YangInt32(7);
    }

    @Test
    public void testHashCode() {
        assertTrue(objType.hashCode() == 0);
        assertTrue(strType.hashCode() == 0);
        assertTrue(shortType1.hashCode() == 7);
        assertTrue(shortType2.hashCode() == 7);
        assertTrue(longType.hashCode() == 7);
    }

    @Test
    public void testEquals() {
        assertTrue(shortType1.equals(shortType1));
        
        // YangInt16 can't equal non-number types
        assertFalse(shortType1.equals(shortType2));
        
        assertTrue(shortType1.equals(longType));
        assertFalse(shortType1.equals(objType));
        assertFalse(objType.equals(shortType1));
        
        assertTrue(shortType1.equals((Object)7));
        assertTrue(shortType1.equals((Object)7L));
        assertFalse(shortType1.equals((Object)"7"));
        assertFalse(shortType1.equals(null));

        assertFalse(strType.equals((Object)"7"));
        strType.value = "7";
        assertTrue(strType.equals((Object)"7"));
    }

    @Test
    public void testToString() {
        assertTrue(shortType1.toString().equals("7"));
        assertTrue(shortType2.toString().equals("7"));
        assertTrue(longType.toString().equals("7"));
    }
    
    @Test(expected=NullPointerException.class)
    public void testToStringException1() {
        assertTrue(objType.toString().equals("null"));
    }
    
    @Test(expected=NullPointerException.class)
    public void testToStringException2() {
        assertTrue(strType.toString().equals("null"));
    }

    @Test
    public void testFromString() throws YangException {
        assertFalse(i32.fromString("7").equals((byte)7));
        assertFalse(i32.fromString("7").equals((short)7));
        assertTrue(i32.fromString("7").equals((int)7));
        assertFalse(i32.fromString("7").equals((long)7));
        
        assertTrue(i32.fromString("7") == (byte)7);
        assertTrue(i32.fromString("7") == (short)7);
        assertTrue(i32.fromString("7") == (int)7);
        assertTrue(i32.fromString("7") == (long)7);

        assertTrue(i32.fromString("-1") == -1);
        
        assertTrue(i32.fromString(Integer.valueOf(Integer.MAX_VALUE).toString())
                == Integer.MAX_VALUE);
        try {
            i32.fromString(Long.valueOf(Integer.MAX_VALUE + 1L).toString());
            fail("Should not be able to parse such a large number");
        } catch (YangException e) {}
        
        assertTrue(i32.fromString(Integer.valueOf(Integer.MIN_VALUE).toString())
                == Integer.MIN_VALUE);
        try {
            i32.fromString(Long.valueOf(Integer.MIN_VALUE - 1L).toString());
            fail("Should not be able to parse such a small number");
        } catch (YangException e) {}

        try {
            i32.fromString("a");
            fail("Should not accept non numbers");
        } catch (YangException e) {}
        try {
            i32.fromString("1a");
            fail("Should not accept strings ending with characters");
        } catch (YangException e) {}
        try {
            i32.fromString("a1");
            fail("Should not accept strings beginning with characters");
        } catch (YangException e) {}
    }

}