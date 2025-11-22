/*
 * Copyright (C) 2019-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.phplib.templateengine.intern.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import de.powerstat.phplib.templateengine.intern.FileManager;
import de.powerstat.phplib.templateengine.intern.VariableManager;


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
  // @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
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
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(FileManager.class).withNonnullFields("files", "variableManager").verify();
   }

 }
