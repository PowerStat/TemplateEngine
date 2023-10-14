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

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.powerstat.phplib.templateengine.intern.FileManager;
import de.powerstat.phplib.templateengine.intern.VariableManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * File manager tests.
 */
final class FileManagerTests
 {
  /**
   * Not constructed constant.
   */
  private static final String NOT_CONSTRUCTED = "Not constructed";

  /**
   * Filename1 constant.
   */
  private static final String FILENAME1 = "filename1";

  /**
   * Template 1 file name constant.
   */
  private static final String TEMPLATE1_FILE = "target/test-classes/templates/template1.tmpl";

  /**
   * Add file failed constant.
   */
  private static final String ADD_FILE_FAILED = "addFile failed";

  /**
   * Null pointer exception constant.
   */
  private static final String NULL_POINTER_EXCEPTION = "Null pointer exception";

  /**
   * Exists file succeeded constant.
   */
  private static final String EXISTS_FILE_SUCCEEDED = "existsFile succeeded";

  /**
   * Exists file failed constant.
   */
  private static final String EXISTS_FILE_FAILED = "existsFile failed";

  /**
   * toSTring result not as expected constant.
   */
  private static final String TO_STRING_RESULT_NOT_AS_EXPECTED = "toString() result not as expected";


  /**
   * Default constructor.
   */
  /* default */ FileManagerTests()
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
    final FileManager fm1 = new FileManager(vm1);
    assertNotNull(fm1, NOT_CONSTRUCTED);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    final FileManager fm2 = new FileManager(vm1, fm1);
    assertNotNull(fm2, NOT_CONSTRUCTED);
   }


  /**
   * addFile test.
   */
  @Test
  /* default */ void testAddFile1()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.addFile(FILENAME1, new File(TEMPLATE1_FILE));
    assertTrue(result, ADD_FILE_FAILED);
   }


  /**
   * addFile test.
   */
  @Test
  /* default */ void testAddFile2()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    final File tmplFile = new File(TEMPLATE1_FILE);
    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ fm1.addFile(null, tmplFile);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * addFile test.
   */
  @Test
  /* default */ void testAddFile3()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.addFile("", new File(TEMPLATE1_FILE));
    assertTrue(result, ADD_FILE_FAILED);
   }


  /**
   * addFile test.
   */
  @Test
  /* default */ void testAddFile4()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.addFile(FILENAME1, new File("target/test-classes/templates/template0.tmpl"));
    assertFalse(result, "addFile succeeded");
   }


  /**
   * addFile test.
   */
  @Test
  /* default */ void testAddFile5()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ fm1.addFile(FILENAME1, null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * existsFile test.
   */
  @Test
  /* default */ void testExistsFile1()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.existsFile(FILENAME1);
    assertFalse(result, EXISTS_FILE_SUCCEEDED);
   }


  /**
   * existsFile test.
   */
  @Test
  /* default */ void testExistsFile2()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    /* boolean result = */ fm1.addFile(FILENAME1, new File(TEMPLATE1_FILE));

    final boolean result = fm1.existsFile(FILENAME1);
    assertTrue(result, EXISTS_FILE_FAILED);
   }


  /**
   * existsFile test.
   */
  @Test
  /* default */ void testExistsFile3()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.existsFile("");
    assertFalse(result, EXISTS_FILE_SUCCEEDED);
   }


  /**
   * existsFile test.
   */
  @Test
  /* default */ void testExistsFile4()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    /* boolean result = */ fm1.addFile("", new File(TEMPLATE1_FILE));

    final boolean result = fm1.existsFile("");
    assertTrue(result, EXISTS_FILE_FAILED);
   }


  /**
   * existsFile test.
   */
  @Test
  @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  /* default */ void testExistsFile5()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ fm1.existsFile(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * loadFile test.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testLoadFile1() throws IOException
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.loadFile(FILENAME1);
    assertFalse(result, EXISTS_FILE_SUCCEEDED);
   }


  /**
   * loadFile test.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testLoadFile2() throws IOException
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final boolean result = fm1.loadFile("");
    assertFalse(result, EXISTS_FILE_SUCCEEDED);
   }


  /**
   * loadFile test.
   */
  @Test
  /* default */ void testLoadFile3()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    assertThrows(NullPointerException.class, () ->
     {
      /* final boolean result = */ fm1.loadFile(null);
     }, NULL_POINTER_EXCEPTION
    );
   }


  /**
   * loadFile test.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testLoadFile4() throws IOException
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    /* boolean result = */ fm1.addFile(FILENAME1, new File(TEMPLATE1_FILE));

    final boolean result = fm1.loadFile(FILENAME1);
    assertTrue(result, "loadFile failed");
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString1()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);

    final String string = fm1.toString();
    assertEquals("FileManager[files=Optional.empty]", string, TO_STRING_RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString2()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    /* final boolean result = */ fm1.addFile(FILENAME1, new File(TEMPLATE1_FILE));

    final String string = fm1.toString();
    assertEquals("FileManager[files=Optional[template1.tmpl]]", string, TO_STRING_RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final VariableManager vm1 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    final FileManager fm2 = new FileManager(vm1);
    final FileManager fm3 = new FileManager(vm1);
    /* final boolean result = */ fm3.addFile(FILENAME1, new File(TEMPLATE1_FILE));
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(fm1.hashCode(), fm2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(fm1.hashCode(), fm3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
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
    vm2.setVar("test1", "test");
    final VariableManager vm3 = new VariableManager();
    final VariableManager vm4 = new VariableManager();
    final FileManager fm1 = new FileManager(vm1);
    final FileManager fm2 = new FileManager(vm1);
    final FileManager fm3 = new FileManager(vm2);
    final FileManager fm4 = new FileManager(vm1);
    final FileManager fm5 = new FileManager(vm3);
    /* final boolean result = */ fm5.addFile(FILENAME1, new File(TEMPLATE1_FILE));
    final FileManager fm6 = new FileManager(vm4);
    /* final boolean result = */ fm6.addFile("filename2", new File("target/test-classes/templates/template2.tmpl"));
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(fm1.equals(fm1), "FileManager11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(fm1.equals(fm2), "FileManager12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(fm2.equals(fm1), "FileManager21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(fm2.equals(fm4), "FileManager24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(fm1.equals(fm4), "FileManager14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(fm1.equals(fm3), "FileManager13 are equal"), //$NON-NLS-1$
      () -> assertFalse(fm3.equals(fm1), "FileManager31 are equal"), //$NON-NLS-1$
      () -> assertFalse(fm1.equals(null), "FileManager10 is equal"), //$NON-NLS-1$
      () -> assertFalse(fm1.equals(fm5), "FileManager15 is equal"), //$NON-NLS-1$
      () -> assertFalse(fm1.equals(fm6), "FileManager16 is equal") //$NON-NLS-1$
    );
   }

 }
