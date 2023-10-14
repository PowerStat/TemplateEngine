/*
 * Copyright (C) 2019-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine.intern.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.powerstat.phplib.templateengine.intern.VariableManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Variable manager tests.
 */
final class VariableManagerTests
 {
  /**
   * Construction failed constant.
   */
  private static final String CONSTRUCTION_FAILED = "Construction failed";

  /**
   * Testvar constant.
   */
  private static final String TESTVAR = "testvar";

  /**
   * Test constant.
   */
  private static final String TEST = "test";

  /**
   * Variable value not as expected constant.
   */
  private static final String VARIABLE_VALUE_NOT_AS_EXPECTED = "Variable value not as expected";

  /**
   * Null pointer exception constant.
   */
  private static final String NULL_POINTER_EXCEPTION = "Null pointer exception";

  /**
   * Result values not as expected constant.
   */
  private static final String RESULT_VALUES_NOT_AS_EXPECTED = "Result values not as expected";

  /**
   * Before substitute after constant.
   */
  private static final String BEFORE_SUBSTITUTE_AFTER = "before{substitute}after";

  /**
   * Substitute constant.
   */
  private static final String SUBSTITUTE = "substitute";

  /**
   * Before test after constant.
   */
  private static final String BEFORETESTAFTER = "beforetestafter";

  /**
   * Target constant.
   */
  private static final String TARGET = "target";

  /**
   * Default constant.
   */
  private static final String DEFAULT = "default";


  /**
   * Default constructor.
   */
  /* default */ VariableManagerTests()
   {
    super();
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor1()
   {
    final VariableManager vm1 = new VariableManager();
    assertNotNull(vm1, CONSTRUCTION_FAILED);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    final VariableManager vm1 = new VariableManager();
    final VariableManager vm2 = new VariableManager(vm1);
    assertNotNull(vm2, CONSTRUCTION_FAILED);
   }


  /**
   * setVar test.
   */
  @Test
  /* default */ void testSetVar1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    assertEquals(TEST, vm1.getVar(TESTVAR), VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * setVar test.
   */
  @Test
  /* default */ void testSetVar2()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      vm1.setVar(null, TEST);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * setVar test.
   */
  @Test
  /* default */ void testSetVar3()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, null);
    assertEquals("", vm1.getVar(TESTVAR), VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * existsVar test.
   */
  @Test
  /* default */ void testExistsVar1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    final boolean result = vm1.existsVar(TESTVAR);
    assertTrue(result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * existsVar test.
   */
  @Test
  /* default */ void testExistsVar2()
   {
    final VariableManager vm1 = new VariableManager();
    final boolean result = vm1.existsVar(TESTVAR);
    assertFalse(result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * existsVar test.
   */
  @Test
  /* default */ void testExistsVar3()
   {
    final VariableManager vm1 = new VariableManager();
    final boolean result = vm1.existsVar("");
    assertFalse(result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * existsVar test.
   */
  @Test
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  /* default */ void testExistsVar4()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ vm1.existsVar(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * getVar test.
   */
  @Test
  /* default */ void testGetVar1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    final String result = vm1.getVar(TESTVAR);
    assertEquals(TEST, result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * getVar test.
   */
  @Test
  /* default */ void testGetVar2()
   {
    final VariableManager vm1 = new VariableManager();
    final String result = vm1.getVar(TESTVAR);
    assertEquals("", result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * getVar test.
   */
  @Test
  /* default */ void testGetVar3()
   {
    final VariableManager vm1 = new VariableManager();
    final String result = vm1.getVar("");
    assertEquals("", result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * getVar test.
   */
  @Test
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  /* default */ void testGetVar4()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      /* final String result = */ vm1.getVar(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * unsetVar test.
   */
  @Test
  /* default */ void testUnsetVar1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    vm1.unsetVar(TESTVAR);
    final String result = vm1.getVar(TESTVAR);
    assertEquals("", result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * unsetVar test.
   */
  @Test
  /* default */ void testUnsetVar2()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.unsetVar(TESTVAR);
    final String result = vm1.getVar(TESTVAR);
    assertEquals("", result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * unsetVar test.
   */
  @Test
  /* default */ void testUnsetVar3()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.unsetVar("");
    final String result = vm1.getVar("");
    assertEquals("", result, VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * unsetVar test.
   */
  @Test
  /* default */ void testUnsetVar4()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      vm1.unsetVar(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * getVars test.
   */
  @Test
  /* default */ void testGetVars1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    final List<String> results = vm1.getVars();
    final String[] expected = {TESTVAR};
    assertArrayEquals(expected, results.toArray(), RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * getVars test.
   */
  @Test
  /* default */ void testGetVars2()
   {
    final VariableManager vm1 = new VariableManager();
    final List<String> results = vm1.getVars();
    final String[] expected = {};
    assertArrayEquals(expected, results.toArray(), RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * getUndefined test.
   */
  @Test
  /* default */ void testGetUndefined1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, "before{undefined}after");
    final List<String> results = vm1.getUndefined(TESTVAR);
    final String[] expected = {"undefined"};
    assertArrayEquals(expected, results.toArray(), RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * getUndefined test.
   */
  @Test
  /* default */ void testGetUndefined2()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    final List<String> results = vm1.getUndefined(TESTVAR);
    final String[] expected = {};
    assertArrayEquals(expected, results.toArray(), RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * getUndefined test.
   */
  @Test
  /* default */ void testGetUndefined3()
   {
    final VariableManager vm1 = new VariableManager();
    final List<String> results = vm1.getUndefined("");
    final String[] expected = {};
    assertArrayEquals(expected, results.toArray(), RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * getUndefined test.
   */
  @Test
  /* default */ void testGetUndefined4()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      /* final List<String> results = */ vm1.getUndefined(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * subst test.
   */
  @Test
  /* default */ void testSubst1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    vm1.setVar(SUBSTITUTE, TEST);
    final String result = vm1.subst(TESTVAR);
    assertEquals(BEFORETESTAFTER, result, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * subst test.
   */
  @Test
  /* default */ void testSubst2()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    final String result = vm1.subst(TESTVAR);
    assertEquals(BEFORE_SUBSTITUTE_AFTER, result, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * subst test.
   */
  @Test
  /* default */ void testSubst3()
   {
    final VariableManager vm1 = new VariableManager();
    final String result = vm1.subst(TESTVAR);
    assertEquals("", result, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * subst test.
   */
  @Test
  /* default */ void testSubst4()
   {
    final VariableManager vm1 = new VariableManager();
    final String result = vm1.subst("");
    assertEquals("", result, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * subst test.
   */
  @Test
  /* default */ void testSubst5()
   {
    final VariableManager vm1 = new VariableManager();
    assertThrows(NullPointerException.class, () ->
     {
      /* final String result = */ vm1.subst(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse1()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    vm1.setVar(SUBSTITUTE, TEST);
    vm1.setVar(TARGET, DEFAULT);
    final String result = vm1.parse(TARGET, TESTVAR, false);
    assertEquals(BEFORETESTAFTER, result, RESULT_VALUES_NOT_AS_EXPECTED);
    final String result2 = vm1.getVar(TARGET);
    assertEquals(BEFORETESTAFTER, result2, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse2()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    vm1.setVar(SUBSTITUTE, TEST);
    vm1.setVar(TARGET, DEFAULT);
    final String result = vm1.parse(TARGET, TESTVAR, true);
    assertEquals(BEFORETESTAFTER, result, RESULT_VALUES_NOT_AS_EXPECTED);
    final String result2 = vm1.getVar(TARGET);
    assertEquals("defaultbeforetestafter", result2, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse3()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TARGET, DEFAULT);
    final String result = vm1.parse(TARGET, "", false);
    assertEquals("", result, RESULT_VALUES_NOT_AS_EXPECTED);
    final String result2 = vm1.getVar(TARGET);
    assertEquals("", result2, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse4()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TARGET, DEFAULT);
    assertThrows(NullPointerException.class, () ->
     {
      /* final String result = */ vm1.parse(TARGET, null, false);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse5()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    vm1.setVar(SUBSTITUTE, TEST);
    vm1.setVar("", DEFAULT);
    final String result = vm1.parse("", TESTVAR, false);
    assertEquals(BEFORETESTAFTER, result, RESULT_VALUES_NOT_AS_EXPECTED);
    final String result2 = vm1.getVar("");
    assertEquals(BEFORETESTAFTER, result2, RESULT_VALUES_NOT_AS_EXPECTED);
   }


  /**
   * parse test.
   */
  @Test
  /* default */ void testParse6()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, BEFORE_SUBSTITUTE_AFTER);
    vm1.setVar(SUBSTITUTE, TEST);
    vm1.setVar("", DEFAULT);
    assertThrows(NullPointerException.class, () ->
     {
      /* final String result = */ vm1.parse(null, TESTVAR, false);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(TESTVAR, TEST);
    final String string = vm1.toString();
    assertEquals("VariableManager[vars=[testvar]]", string, "toString() result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final VariableManager vm1 = new VariableManager();
    final VariableManager vm2 = new VariableManager();
    final VariableManager vm3 = new VariableManager();
    vm3.setVar("testvar2", "test2");
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(vm1.hashCode(), vm2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(vm1.hashCode(), vm3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressFBWarnings({"EC_NULL_ARG", "CE_CLASS_ENVY"})
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final VariableManager vm1 = new VariableManager();
    final VariableManager vm2 = new VariableManager();
    final VariableManager vm3 = new VariableManager();
    vm3.setVar("parent1", "before<!-- BEGIN blktest1 -->content<!-- END blktest1 -->after");
    final VariableManager vm4 = new VariableManager();
    final VariableManager vm5 = new VariableManager();
    vm5.setVar("parent3", "before<!-- BEGIN blktest3 -->content<!-- END blktest3 -->after");
    final VariableManager vm6 = new VariableManager();
    vm6.setVar("parent4", "before<!-- BEGIN blktest4 -->content<!-- END blktest4 -->after");

    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(vm1.equals(vm1), "VariableManager11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(vm1.equals(vm2), "VariableManager12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(vm2.equals(vm1), "VariableManager21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(vm2.equals(vm4), "VariableManager24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(vm1.equals(vm4), "VariableManager14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(vm1.equals(vm3), "VariableManager13 are equal"), //$NON-NLS-1$
      () -> assertFalse(vm3.equals(vm1), "VariableManager31 are equal"), //$NON-NLS-1$
      () -> assertFalse(vm1.equals(null), "VariableManager10 is equal"), //$NON-NLS-1$
      () -> assertFalse(vm1.equals(vm5), "VariableManager15 is equal"), //$NON-NLS-1$
      () -> assertFalse(vm1.equals(vm6), "VariableManager16 is equal") //$NON-NLS-1$
    );
   }

 }
