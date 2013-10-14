package org.testory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testory.Testory.givenTest;
import static org.testory.test.TestUtils.readDeclaredFields;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

public class Describe_Testory_givenTest {
  @Test
  public void should_inject_concrete_class() {
    class ConcreteClass {}
    class TestClass {
      ConcreteClass field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertTrue(test.field instanceof ConcreteClass);
  }

  @Test
  public void should_inject_interface() {
    class TestClass {
      Interface field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertTrue(test.field instanceof Interface);
  }

  @Test
  public void should_inject_abstract_class() {
    abstract class AbstractClass {}
    class TestClass {
      AbstractClass field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertTrue(test.field instanceof AbstractClass);
  }

  @Test
  public void should_inject_object_class() {
    class TestClass {
      Object field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertTrue(test.field instanceof Object);
  }

  @Test
  public void should_stub_to_string_to_return_field_name() {
    class TestClass {
      Object field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals("field", test.field.toString());
  }

  @Test
  public void should_stub_equals_to_match_same_instance() {
    class TestClass {
      Object field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertTrue(test.field.equals(test.field));
  }

  @Test
  public void should_stub_equals_to_not_match_not_same_instance() {
    class TestClass {
      Object field;
      Object otherField;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertFalse(test.field.equals(test.otherField));
    assertFalse(test.otherField.equals(test.field));
  }

  @Test
  public void should_stub_equals_to_not_match_null() {
    class TestClass {
      Object field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertFalse(test.field.equals(null));
  }

  @Test
  public void should_stub_hashcode_to_obey_contract() {
    class TestClass {
      Object field, otherField;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals(test.field.hashCode(), test.field.hashCode());
    assertTrue(test.field.hashCode() != test.otherField.hashCode());
  }

  @Test
  public void should_inject_boolean() {
    @SuppressWarnings("unused")
    class TestClass {
      boolean p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Boolean w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    List<Object> fields = readDeclaredFields(test);
    assertTrue(fields.contains(false));
    assertTrue(fields.contains(true));
  }

  @Test
  public void should_inject_character() {
    @SuppressWarnings("unused")
    class TestClass {
      char p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Character w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Character character = (Character) object;
      assertTrue('a' <= character && character <= 'z');
    }
  }

  @Test
  public void should_inject_byte() {
    @SuppressWarnings("unused")
    class TestClass {
      byte p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Byte w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Byte number = (Byte) object;
      assertTrue("" + number, 2 <= Math.abs(number) && Math.abs(number) <= 5);
    }
  }

  @Test
  public void should_inject_short() {
    @SuppressWarnings("unused")
    class TestClass {
      short p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Short w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Short number = (Short) object;
      assertTrue("" + number, 2 <= Math.abs(number) && Math.abs(number) <= 31);
    }
  }

  @Test
  public void should_inject_integer() {
    @SuppressWarnings("unused")
    class TestClass {
      int p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Integer w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Integer number = (Integer) object;
      assertTrue("" + number, 2 <= Math.abs(number) && Math.abs(number) <= 1290);
    }
  }

  @Test
  public void should_inject_long() {
    @SuppressWarnings("unused")
    class TestClass {
      long p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Long w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Long number = (Long) object;
      assertTrue("" + number, 2 <= Math.abs(number) && Math.abs(number) <= 2097152);
    }
  }

  @Test
  public void should_inject_float() {
    @SuppressWarnings("unused")
    class TestClass {
      float p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Float w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Float number = (Float) object;
      assertTrue("" + number,
          Math.pow(2, -30) <= Math.abs(number) && Math.abs(number) <= Math.pow(2, 30));
    }
  }

  @Test
  public void should_inject_double() {
    @SuppressWarnings("unused")
    class TestClass {
      double p0, p1, p2, p3, p4, p5, p6, p7, p8, p9;
      Double w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
    }
    TestClass test = new TestClass();
    givenTest(test);

    for (Object object : readDeclaredFields(test)) {
      Double number = (Double) object;
      assertTrue("" + number,
          Math.pow(2, -300) <= Math.abs(number) && Math.abs(number) <= Math.pow(2, 300));
    }
  }

  @Test
  public void should_inject_string() {
    class TestClass {
      String field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals("field", test.field);
  }

  @Test
  public void should_inject_array_of_primitives() {
    class TestClass {
      int[] ints;
      Integer[] intWrappers;
      float[] floats;
      Float[] floatWrappers;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertNotEquals(0, test.ints[0]);
    assertNotNull(test.intWrappers[0]);
    assertNotEquals(0, test.floats[0]);
    assertNotNull(test.floatWrappers[0]);
  }

  @Test
  public void should_inject_array_of_strings() {
    class TestClass {
      String[] strings;
      String[][] deepStrings;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertArrayEquals(new String[] { "strings[0]" }, test.strings);
    assertArrayEquals(new String[][] { new String[] { "deepStrings[0][0]" } }, test.deepStrings);
  }

  @Test
  public void should_inject_array_of_mocks() {
    class TestClass {
      Object[] objects;
      Object[][] deepObjects;
    }
    TestClass test = new TestClass();
    givenTest(test);

    assertEquals(1, test.objects.length);
    assertNotNull(test.objects[0]);
    assertTrue(test.objects[0].equals(test.objects[0]));
    assertEquals("objects[0]", test.objects[0].toString());

    assertEquals(1, test.deepObjects.length);
    assertEquals(1, test.deepObjects[0].length);
    assertNotNull(test.deepObjects[0][0]);
    assertTrue(test.deepObjects[0][0].equals(test.deepObjects[0][0]));
    assertEquals("deepObjects[0][0]", test.deepObjects[0][0].toString());
  }

  @Test
  public void should_inject_reflection_classes() {
    class TestClass {
      Class<?> clazz;
      Field field;
      Method method;
      Constructor<?> constructor;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertNotNull(test.clazz);
    assertNotNull(test.field);
    assertNotNull(test.method);
    assertNotNull(test.constructor);
    assertEquals(test.clazz, test.field.getDeclaringClass());
    assertEquals(test.clazz, test.method.getDeclaringClass());
    assertEquals(test.clazz, test.constructor.getDeclaringClass());
  }

  @Test
  public void should_inject_enum() {
    class TestClass {
      ElementType field;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals(ElementType.class, test.field.getClass());
  }

  @Test
  public void should_skip_not_null() {
    class TestClass {
      Object field;
      String stringField = "value";
      Boolean booleanField = Boolean.TRUE;
      Character characterField = Character.valueOf((char) 1);
      Byte byteField = Byte.valueOf((byte) 1);
      Short shortField = Short.valueOf((short) 1);
      Integer integerField = Integer.valueOf(1);
      Long longField = Long.valueOf(1);
      Float floatField = Float.valueOf(1);
      Double doubleField = Double.valueOf(1);
    }
    TestClass test = new TestClass();
    Object object = new Object();
    test.field = object;
    givenTest(test);
    assertSame(object, test.field);
    assertEquals("value", test.stringField);
    assertEquals(Boolean.TRUE, test.booleanField);
    assertEquals(Character.valueOf((char) 1), test.characterField);
    assertEquals(Byte.valueOf((byte) 1), test.byteField);
    assertEquals(Short.valueOf((short) 1), test.shortField);
    assertEquals(Integer.valueOf(1), test.integerField);
    assertEquals(Long.valueOf(1), test.longField);
    assertEquals(Float.valueOf(1), test.floatField);
    assertEquals(Double.valueOf(1), test.doubleField);
  }

  @Test
  public void should_skip_void() {
    class TestClass {
      Void voidField;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals(null, test.voidField);
  }

  @Test
  public void should_skip_primitive_not_equal_to_binary_zero() {
    class TestClass {
      boolean booleanPrimitive = true;
      boolean booleanWrapper = true;
      char charField = 'a';
      byte byteField = 1;
      short shortField = 1;
      int intField = 1;
      long longField = 1;
      float floatField = 1;
      double doubleField = 1;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertEquals(true, test.booleanPrimitive);
    assertEquals(true, test.booleanWrapper);
    assertEquals('a', test.charField);
    assertEquals(1, test.byteField);
    assertEquals(1, test.shortField);
    assertEquals(1, test.intField);
    assertEquals(1, test.longField);
    assertEquals(1, test.floatField, 0);
    assertEquals(1, test.doubleField, 0);
  }

  static class TestClassWithStaticField {
    static Object field = null;
  }

  @Test
  public void should_skip_static_field() {
    TestClassWithStaticField test = new TestClassWithStaticField();
    givenTest(test);
    assertNull(TestClassWithStaticField.field);
  }

  @Test
  public void should_skip_final_field() {
    class TestClass {
      final Object field = null;
    }
    TestClass test = new TestClass();
    givenTest(test);
    assertNull(test.field);
  }

  @Test
  public void should_fail_for_final_class() {
    final class FinalClass {}
    class TestClass {
      @SuppressWarnings("unused")
      FinalClass field;
    }
    TestClass test = new TestClass();
    try {
      givenTest(test);
      fail();
    } catch (TestoryException e) {}
  }

  public static interface Interface {}
}
