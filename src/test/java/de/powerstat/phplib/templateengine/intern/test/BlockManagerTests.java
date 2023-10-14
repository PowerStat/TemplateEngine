/*
 * Copyright (C) 2019-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine.intern.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.powerstat.phplib.templateengine.intern.BlockManager;
import de.powerstat.phplib.templateengine.intern.VariableManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Block manager tests.
 */
final class BlockManagerTests
 {
  /**
   * Not constructed constant.
   */
  private static final String NOT_CONSTRUCTED = "Not constructed";

  /**
   * Parent constant.
   */
  private static final String PARENT = "parent";

  /**
   * Block string constant.
   */
  private static final String BLKSTRING = "before<!-- BEGIN blktest -->content<!-- END blktest -->after";

  /**
   * Block test constant.
   */
  private static final String BLKTEST = "blktest";

  /**
   * Block name constant.
   */
  private static final String BLKNAME = "blkname";

  /**
   * Block could not be set constant.
   */
  private static final String BLOCK_COULD_NOT_BE_SET = "Block could not be set";

  /**
   * Parent not as expected constant.
   */
  private static final String PARENT_NOT_AS_EXPECTED = "parent not as expected";

  /**
   * Content constant.
   */
  private static final String CONTENT = "content";

  /**
   * Block test not as expected.
   */
  private static final String BLKTEST_NOT_AS_EXPECTED = "blktest not as expected";

  /**
   * Null pointer exception constant.
   */
  private static final String NULL_POINTER_EXCEPTION = "Null pointer exception";

  /**
   * Before blktest after constant.
   */
  private static final String BEFORE_BLKTEST_AFTER = "before{blktest}after";


  /**
   * Default constructor.
   */
  /* default */ BlockManagerTests()
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
    final BlockManager bm1 = new BlockManager(vm1);
    assertNotNull(bm1, NOT_CONSTRUCTED);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    final BlockManager bm2 = new BlockManager(vm1, bm1);
    assertNotNull(bm2, NOT_CONSTRUCTED);
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock1()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    final boolean result = bm1.setBlock(PARENT, BLKTEST, BLKNAME);
    assertTrue(result, BLOCK_COULD_NOT_BE_SET);
    final String parent = vm1.getVar(PARENT);
    final String blktest = vm1.getVar(BLKTEST);
    assertEquals("before{blkname}after", parent, PARENT_NOT_AS_EXPECTED);
    assertEquals(CONTENT, blktest, BLKTEST_NOT_AS_EXPECTED);
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock2()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ bm1.setBlock(null, BLKTEST, BLKNAME);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock3()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    assertThrows(IllegalStateException.class, () ->
     {
      /* final boolean result = */ bm1.setBlock(PARENT, null, BLKNAME);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock4()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    final boolean result = bm1.setBlock(PARENT, BLKTEST, null);
    assertTrue(result, BLOCK_COULD_NOT_BE_SET);
    final String parent = vm1.getVar(PARENT);
    final String blktest = vm1.getVar(BLKTEST);
    assertEquals(BEFORE_BLKTEST_AFTER, parent, PARENT_NOT_AS_EXPECTED);
    assertEquals(CONTENT, blktest, BLKTEST_NOT_AS_EXPECTED);
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock5()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    assertThrows(IllegalStateException.class, () ->
     {
      /* final boolean result = */ bm1.setBlock("", BLKTEST, BLKNAME);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock6()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    assertThrows(IllegalStateException.class, () ->
     {
      /* final boolean result = */ bm1.setBlock(PARENT, "", BLKNAME);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * Set block test.
   */
  @Test
  /* default */ void testSetBlock7()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    final boolean result = bm1.setBlock(PARENT, BLKTEST, "");
    assertTrue(result, BLOCK_COULD_NOT_BE_SET);
    final String parent = vm1.getVar(PARENT);
    final String blktest = vm1.getVar(BLKTEST);
    assertEquals(BEFORE_BLKTEST_AFTER, parent, PARENT_NOT_AS_EXPECTED);
    assertEquals(CONTENT, blktest, BLKTEST_NOT_AS_EXPECTED);
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final VariableManager vm1 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    vm1.setVar(PARENT, BLKSTRING);
    /* final boolean result = */ bm1.setBlock(PARENT, BLKTEST, BLKNAME);
    final String string = bm1.toString();
    assertEquals("BlockManager[vars=[parent, blktest]]", string, "toString() result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final VariableManager vm1 = new VariableManager();
    vm1.setVar(PARENT, BLKSTRING);
    final VariableManager vm2 = new VariableManager();
    final BlockManager bm1 = new BlockManager(vm1);
    final BlockManager bm2 = new BlockManager(vm1);
    final BlockManager bm3 = new BlockManager(vm2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(bm1.hashCode(), bm2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(bm1.hashCode(), bm3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressFBWarnings("EC_NULL_ARG")
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final VariableManager vm1 = new VariableManager();
    final VariableManager vm2 = new VariableManager();
    vm2.setVar(PARENT, BLKSTRING);
    final VariableManager vm3 = new VariableManager();
    vm3.setVar("parent2", "before<!-- BEGIN blktest2 -->content<!-- END blktest2 -->after");
    final VariableManager vm4 = new VariableManager();
    vm4.setVar("parent3", "before<!-- BEGIN blktest3 -->content<!-- END blktest3 -->after");
    final BlockManager bm1 = new BlockManager(vm1);
    final BlockManager bm2 = new BlockManager(vm1);
    final BlockManager bm3 = new BlockManager(vm2);
    final BlockManager bm4 = new BlockManager(vm1);
    final BlockManager bm5 = new BlockManager(vm3);
    final BlockManager bm6 = new BlockManager(vm4);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(bm1.equals(bm1), "BlockManager11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(bm1.equals(bm2), "BlockManager12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(bm2.equals(bm1), "BlockManager21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(bm2.equals(bm4), "BlockManager24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(bm1.equals(bm4), "BlockManager14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(bm1.equals(bm3), "BlockManager13 are equal"), //$NON-NLS-1$
      () -> assertFalse(bm3.equals(bm1), "BlockManager31 are equal"), //$NON-NLS-1$
      () -> assertFalse(bm1.equals(null), "BlockManager10 is equal"), //$NON-NLS-1$
      () -> assertFalse(bm1.equals(bm5), "BlockManager15 is equal"), //$NON-NLS-1$
      () -> assertFalse(bm1.equals(bm6), "BlockManager16 is equal") //$NON-NLS-1$
    );
   }

 }
