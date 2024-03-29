/*
 * Copyright (C) 2019-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import de.powerstat.phplib.templateengine.HandleUndefined;
import de.powerstat.phplib.templateengine.TemplateEngine;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * TemplateEngine tests.
 */
@SuppressFBWarnings({"CE_CLASS_ENVY", "EC_NULL_ARG"})
@SuppressWarnings({"PMD.LinguisticNaming", "PMD.ExcessiveClassLength", "PMD.LongVariable", "PMD.ExcessivePublicCount"})
final class TemplateEngineTests
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(TemplateEngineTests.class);

  /**
   * Template file 1 path constant.
   */
  private static final String TEMPLATE1_TMPL = "target/test-classes/templates/template1.tmpl"; //$NON-NLS-1$

  /**
   * Template file 2 path constant.
   */
  private static final String TEMPLATE2_TMPL = "target/test-classes/templates/template2.tmpl"; //$NON-NLS-1$

  /**
   * Template file 3 path constant.
   */
  private static final String TEMPLATE3_TMPL = "target/test-classes/templates/template3.tmpl";

  /**
   * Template 4 path.
   */
  private static final String TEMPLATE4_TMPL = "target/test-classes/templates/template4.tmpl"; //$NON-NLS-1$

  /**
   * Template 8 path.
   */
  private static final String TEMPLATE8_TMPL = "target/test-classes/templates/template8.tmpl"; //$NON-NLS-1$

  /**
   * Template 9 path (maximum file size).
   */
  private static final String TEMPLATE9_TMPL = "target/test-classes/templates/template9.tmpl"; //$NON-NLS-1$

  /**
   * Template 10 path (file to large).
   */
  private static final String TEMPLATE10_TMPL = "target/test-classes/templates/template10.tmpl"; //$NON-NLS-1$

  /**
   * Illegal argument exception expected.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$

  /**
   * Assertion error expected message.
   */
  private static final String ASSERTION_ERROR_EXPECTED = "Assertion error expected"; //$NON-NLS-1$

  /**
   * Found more or less undefined variables message.
   */
  private static final String FOUND_MORE_OR_LESS_UNDEFINED_VARIABLES = "Found more or less undefined variables"; //$NON-NLS-1$

  /**
   * Not found expected undefined variable message.
   */
  private static final String NOT_FOUND_EXPECTED_UNDEFINED_VARIABLE = "Not found expected undefined variable"; //$NON-NLS-1$

  /**
   * Template file 1 name constant.
   */
  private static final String FILE1 = "file1"; //$NON-NLS-1$

  /**
   * Template file 2 name constant.
   */
  private static final String FILE2 = "file2"; //$NON-NLS-1$

  /**
   * Template file 3 name constant.
   */
  private static final String FILE3 = "file3"; //$NON-NLS-1$

  /**
   * Template file 4 name constant.
   */
  private static final String FILE4 = "file4"; //$NON-NLS-1$

  /**
   * Template variable 1 name constant.
   */
  private static final String VARIABLE1 = "variable1"; //$NON-NLS-1$

  /**
   * Template variable 2 name constant.
   */
  private static final String VARIABLE2 = "variable2"; //$NON-NLS-1$

  /**
   * Template variable 3 name constant.
   */
  private static final String VARIABLE3 = "variable3"; //$NON-NLS-1$

  /**
   * Template variable 4 name constant with illegal character.
   */
  private static final String VARIABLE4 = "file~"; //$NON-NLS-1$

  /**
   * Test 0.
   */
  private static final String TEST0 = "test0";

  /**
   * Value 1.
   */
  private static final String VALUE1 = "TEST1"; //$NON-NLS-1$

  /**
   * Value 2.
   */
  private static final String VALUE2 = "TEST2"; //$NON-NLS-1$

  /**
   * Value 3.
   */
  private static final String VALUE3 = "TEST3"; //$NON-NLS-1$

  /**
   * Maximum allowed varname.
   */
  private static final String MAX_VARNAME = "1234567890123456789012345678901234567890123456789012345678901234"; //$NON-NLS-1$

  /**
   * To long varname.
   */
  private static final String TO_LONG_VARNAME = "12345678901234567890123456789012345678901234567890123456789012345"; //$NON-NLS-1$

  /**
   * Template variable value constant.
   */
  private static final String TEST = "TEST"; //$NON-NLS-1$

  /**
   * Template block 1 block name constant.
   */
  private static final String BLK1_BLK = "BLK1_BLK"; //$NON-NLS-1$

  /**
   * Template block 1 name constant.
   */
  private static final String BLK1 = "BLK1"; //$NON-NLS-1$

  /**
   * Template output variable name constant.
   */
  private static final String OUTPUT = "output"; //$NON-NLS-1$

  /**
   * Template constant.
   */
  private static final String TEMPLATE = "template"; //$NON-NLS-1$

  /**
   * No template found message.
   */
  private static final String NO_TEMPLATE_FOUND = "No 'template' found"; //$NON-NLS-1$

  /**
   * Variable value not as expected message.
   */
  private static final String VARIABLE_VALUE_NOT_AS_EXPECTED = "Variable value not as expected"; //$NON-NLS-1$

  /**
   * Set block not as expected message.
   */
  private static final String SET_BLOCK_NOT_AS_EXPECTED = "SetBlock not as expected"; //$NON-NLS-1$

  /**
   * Template file template1.tmpl could not be loaded message.
   */
  private static final String TEMPLATE_FILE_TEMPLATE1_TMPL_COULD_NOT_BE_LOADED = "Template file template1.tmpl could not be loaded!"; //$NON-NLS-1$

  /**
   * Equals block.
   */
  private static final String EQUALS_123_VARIABLE1_456 = "123\n{variable1}\n456\n"; //$NON-NLS-1$

  /**
   * Undefined variable(s) found message.
   */
  private static final String UNDEFINED_VARIABLE_S_FOUND = "Undefined variable(s) found!"; //$NON-NLS-1$

  /**
   * Output not as expected message.
   */
  private static final String OUTPUT_NOT_AS_EXPECTED = "Output not as expected"; //$NON-NLS-1$

  /**
   * Illegal state exception expected message.
   */
  private static final String ILLEGAL_STATE_EXCEPTION_EXPECTED = "Illegal state exception expected"; //$NON-NLS-1$

  /**
   * Block value not as expected message.
   */
  private static final String BLOCK_VALUE_NOT_AS_EXPECTED = "Block value not as expected"; //$NON-NLS-1$

  /**
   * Block test value.
   */
  private static final String VALUE_1234 = "1234"; //$NON-NLS-1$

  /**
   * Output value not as expected message.
   */
  private static final String OUTPUT_VALUE_NOT_AS_EXPECTED = "Output value not as expected"; //$NON-NLS-1$

  /**
   * Parse result not as expected message.
   */
  private static final String PARSE_RESULT_NOT_AS_EXPECTED = "Parse result not as expected"; //$NON-NLS-1$

  /**
   * File 0 name.
   */
  private static final String FILE0 = "file0"; //$NON-NLS-1$

  /**
   * File 5 name.
   */
  private static final String FILE5 = "file5"; //$NON-NLS-1$

  /**
   * Filename template0.tmpl.
   */
  private static final String TEMPLATE0_TMPL = "template0.tmpl"; //$NON-NLS-1$

  /**
   * No undefined variable(s) found message.
   */
  private static final String NO_UNDEFINED_VARIABLE_S_FOUND = "No undefined variable(s) found!"; //$NON-NLS-1$

  /**
   * Undefined variable not as expected message.
   */
  private static final String UNDEFINED_VARIABLE_NOT_AS_EXPECTED = "Undefined variable not as expected"; //$NON-NLS-1$

  /**
   * Null pointer exception expected message.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$

  /**
   * newInstance result not as expected message.
   */
  private static final String NEW_INSTANCE_RESULT_NOT_AS_EXPECTED = "newInstance result not as expected"; //$NON-NLS-1$

  /**
   * Parent constant.
   */
  private static final String PARENT = "parent";


  /**
   * Default constructor.
   */
  /* default */ TemplateEngineTests()
   {
    super();
   }


  /* *
   * Log template engine variables for debugging.
   *
   * @param engine TemplateEngine
   */
  /*
  private static void logVars(final TemplateEngine engine)
   {
    final String[] variables = engine.getVars();
    LOGGER.info("Number of variables: " + variables.length); //$NON-NLS-1$
    for (final String varname : variables)
     {
      LOGGER.info("variables: " + varname + " = " + engine.getVar(varname));  //$NON-NLS-1$//$NON-NLS-2$
     }
   }
  */


  /* *
   * Log undefined variables for debugging.
   *
   * @param engine TemplateEngine
   * @param varname Variable name for which get the undefined variables
   * @throws IOException IO exception
   */
  /*
  private static void logUndefVars(final TemplateEngine engine, final String varname) throws IOException
   {
    final List<String> undefinedVariables = engine.getUndefined(varname);
    LOGGER.info("length: " + undefinedVariables.size()); //$NON-NLS-1$
    for (final String undefVarname : undefinedVariables)
     {
      LOGGER.info("variables: " + undefVarname); //$NON-NLS-1$
     }
   }
  */


  /**
   * Test default constructor.
   */
  @Test
  /* default */ void testDefaultConstructor()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertNotNull(engine, "Default constructor failed!"); //$NON-NLS-1$
   }


  /**
   * Test set template file.
   */
  @Test
  /* default */ void testSetFile()
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    assertTrue(success, TemplateEngineTests.TEMPLATE_FILE_TEMPLATE1_TMPL_COULD_NOT_BE_LOADED);
   }


  /**
   * Test set template file with max length.
   */
  @Test
  /* default */ void testSetFileMaxLength()
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(TemplateEngineTests.MAX_VARNAME, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    assertTrue(success, TemplateEngineTests.TEMPLATE_FILE_TEMPLATE1_TMPL_COULD_NOT_BE_LOADED);
   }


  /**
   * Test set template file.
   */
  @Test
  /* default */ void testSetFileEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    final File tmplFile = new File(TemplateEngineTests.TEMPLATE1_TMPL);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean success = */ engine.setFile("", tmplFile); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set template file with to long filename.
   */
  @Test
  /* default */ void testSetFileToLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    final File tmplFile = new File(TemplateEngineTests.TEMPLATE1_TMPL);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean success = */ engine.setFile(TemplateEngineTests.TO_LONG_VARNAME, tmplFile);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set template file with wrong filename.
   */
  @Test
  /* default */ void testSetFileWrongname()
   {
    final TemplateEngine engine = new TemplateEngine();
    final File templFile = new File(TemplateEngineTests.TEMPLATE1_TMPL);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean success = */ engine.setFile("file~1", templFile); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set template file with to large file.
   */
  @Test
  /* default */ void testSetFileToLarge()
   {
    final TemplateEngine engine = new TemplateEngine();
    final File templFile = new File(TemplateEngineTests.TEMPLATE10_TMPL);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean success = */ engine.setFile(TemplateEngineTests.VARIABLE1, templFile);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test read template file.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSubst() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    final String variableValue = engine.subst(TemplateEngineTests.FILE1);
    TemplateEngineTests.LOGGER.debug("file1 = {}", variableValue); //$NON-NLS-1$
    assertEquals(TemplateEngineTests.EQUALS_123_VARIABLE1_456, variableValue, TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * Test read empty template file.
   */
  @Test
  /* default */ void testSubstEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String variableValue = */ engine.subst(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test subst with to long varname.
   */
  @Test
  /* default */ void testSubstToLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String variableValue = */ engine.subst(TemplateEngineTests.TO_LONG_VARNAME);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test subst with wrong varname.
   */
  @Test
  /* default */ void testSubstWrongName()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String variableValue = */ engine.subst(TemplateEngineTests.VARIABLE4);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test subst with null.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSubstNull() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, null);
    final String variableValue = engine.subst(TemplateEngineTests.VARIABLE1);
    assertEquals("", variableValue, "Unexpected value");
   }


  /**
   * Test with to large template.
   */
  @Test
  /* default */ void testToLargeTemplate()
   {
    final TemplateEngine engine = new TemplateEngine();
    final File templFile = new File(TemplateEngineTests.TEMPLATE10_TMPL);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, templFile);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test subst with max varname length varname.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSubstMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    final String variableValue = engine.subst(TemplateEngineTests.MAX_VARNAME);
    assertEquals("", variableValue, "Subst result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get undefined variables from template.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetUndefined1() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertEquals(1, undefinedVars.size(), TemplateEngineTests.FOUND_MORE_OR_LESS_UNDEFINED_VARIABLES),
      () -> assertEquals(TemplateEngineTests.VARIABLE1, undefinedVars.get(0), TemplateEngineTests.NOT_FOUND_EXPECTED_UNDEFINED_VARIABLE)
    );
   }


  /**
   * Get undefined variables from template.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetUndefined2() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE3, new File(TemplateEngineTests.TEMPLATE3_TMPL));
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE3);
    final int expectedSize = 3;
    assertAll(
      () -> assertEquals(expectedSize, undefinedVars.size(), TemplateEngineTests.FOUND_MORE_OR_LESS_UNDEFINED_VARIABLES),
      () -> assertEquals(TemplateEngineTests.TEST0, undefinedVars.get(0), TemplateEngineTests.NOT_FOUND_EXPECTED_UNDEFINED_VARIABLE)
    );
   }


  /**
   * Get undefined variables from empty template.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetUndefinedFromEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE4);
    assertEquals(0, undefinedVars.size(), "Found undefined variables"); //$NON-NLS-1$
   }


  /**
   * Get empty undefined variables.
   */
  @Test
  /* default */ void testGetUndefinedEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final List<String> undefinedVars = */ engine.getUndefined(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Get undefined variables with to long varname.
   */
  @Test
  /* default */ void testGetUndefinedToLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final List<String> undefinedVars = */ engine.getUndefined(TemplateEngineTests.TO_LONG_VARNAME);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Get undefined variables with wrong varname.
   */
  @Test
  /* default */ void testGetUndefinedWrongName()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final List<String> undefinedVars = */ engine.getUndefined(TemplateEngineTests.VARIABLE4);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Get undefined variables with maximum varname length.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetUndefinedMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.MAX_VARNAME);
    assertEquals(new ArrayList<>(), undefinedVars, "GetUndefined result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test set variable as empty.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetVarEmpty() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1);
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertTrue(undefinedVars.isEmpty(), TemplateEngineTests.UNDEFINED_VARIABLE_S_FOUND),
      () -> assertTrue(engine.getVar(TemplateEngineTests.VARIABLE1).isEmpty(), "variable1 is not empty!") //$NON-NLS-1$
    );
   }


  /**
   * Test set variable with value.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetVar() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TEST);
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertTrue(undefinedVars.isEmpty(), TemplateEngineTests.UNDEFINED_VARIABLE_S_FOUND),
      () -> assertEquals(TemplateEngineTests.TEST, engine.getVar(TemplateEngineTests.VARIABLE1), TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED)
    );
   }


  /**
   * Test set variable with null value.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetVarNull() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, null);
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertTrue(undefinedVars.isEmpty(), TemplateEngineTests.UNDEFINED_VARIABLE_S_FOUND),
      () -> assertEquals("", engine.getVar(TemplateEngineTests.VARIABLE1), TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED) //$NON-NLS-1$
    );
   }


  /**
   * Test set empty variable with value.
   */
  @Test
  /* default */ void testSetEmptyVar()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.setVar("", TemplateEngineTests.TEST); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set variable with maximum long name.
   */
  @Test
  /* default */ void testSetVarMaxLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    engine.setVar(TemplateEngineTests.MAX_VARNAME, TemplateEngineTests.TEST);
    assertEquals(TemplateEngineTests.TEST, engine.getVar(TemplateEngineTests.MAX_VARNAME), TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * Test set variable with to long name.
   */
  @Test
  /* default */ void testSetVarToLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.setVar(TemplateEngineTests.TO_LONG_VARNAME, TemplateEngineTests.TEST);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set variable with wrong name.
   */
  @Test
  /* default */ void testSetVarWrongName()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.setVar(TemplateEngineTests.VARIABLE4, TemplateEngineTests.TEST);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set variable with maximum value size.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetVarMaxLarge() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    engine.setVar(TemplateEngineTests.VARIABLE1, readStringFromFile(new File(TemplateEngineTests.TEMPLATE9_TMPL)));
    assertNotNull(engine.getVar(TemplateEngineTests.VARIABLE1), TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * Test set variable with to large value size.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetVarToLarge() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final String templ = readStringFromFile(new File(TemplateEngineTests.TEMPLATE10_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.setVar(TemplateEngineTests.VARIABLE1, templ);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test get removed variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetRemoved() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    final String parseResult = engine.parse(TemplateEngineTests.OUTPUT, TemplateEngineTests.FILE1);
    final String output = engine.get(TemplateEngineTests.OUTPUT);
    assertAll(
      () -> assertNotNull(parseResult, "Parse result is null!"), //$NON-NLS-1$
      () -> assertEquals("123\n\n456\n", output, TemplateEngineTests.OUTPUT_NOT_AS_EXPECTED) //$NON-NLS-1$
    );
   }


  /**
   * Test get keep variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetKeep() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.KEEP);
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    /* String parseResult = */ engine.parse(TemplateEngineTests.OUTPUT, TemplateEngineTests.FILE1);
    final String output = engine.get(TemplateEngineTests.OUTPUT);
    assertEquals(TemplateEngineTests.EQUALS_123_VARIABLE1_456, output, "Output not as exptected"); //$NON-NLS-1$
   }


  /**
   * Test get replace with comment variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testGetComment() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    /* String parseResult = */ engine.parse(TemplateEngineTests.OUTPUT, TemplateEngineTests.FILE1);
    final String output = engine.get(TemplateEngineTests.OUTPUT);
    assertEquals("123\n<!-- Template variable 'variable1' undefined -->\n456\n", output, TemplateEngineTests.OUTPUT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test set non existing block.
   */
  @Test
  /* default */ void testSetBlockFailure()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    assertThrows(IllegalStateException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, "blk2"); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_STATE_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set empty block name.
   */
  @Test
  /* default */ void testSetBlockEmpty()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock("", ""); //$NON-NLS-1$ //$NON-NLS-2$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set empty block name.
   */
  @Test
  /* default */ void testSetBlockMixed()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, ""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set existing block.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlock() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    final boolean successBlock = engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    final String block = engine.getVar(TemplateEngineTests.BLK1);
    assertAll(
      () -> assertTrue(successBlock, "Block could not be cut out successfully!"), //$NON-NLS-1$
      () -> assertEquals("\n789\n{variable2}\nabc\n", block, TemplateEngineTests.BLOCK_VALUE_NOT_AS_EXPECTED) //$NON-NLS-1$
    );
   }


  /**
   * Test set block with to long parent name.
   */
  @Test
  /* default */ void testSetBlockParentToLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.TO_LONG_VARNAME, TemplateEngineTests.VARIABLE1, ""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with to long varname.
   */
  @Test
  /* default */ void testSetBlockVarnameToLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TO_LONG_VARNAME, ""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with to long name.
   */
  @Test
  /* default */ void testSetBlockNameToLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE_1234, TemplateEngineTests.TO_LONG_VARNAME);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with to wrong parent name.
   */
  @Test
  /* default */ void testSetBlockParentWrong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.VARIABLE4, TemplateEngineTests.VARIABLE1, ""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with wrong varname.
   */
  @Test
  /* default */ void testSetBlockVarnameWrong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VARIABLE4, ""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with wrong name.
   */
  @Test
  /* default */ void testSetBlockNameWrong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE_1234, TemplateEngineTests.VARIABLE4);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test set block with to max length parent name.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlockParentMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(TemplateEngineTests.MAX_VARNAME, TemplateEngineTests.VARIABLE1, ""); //$NON-NLS-1$
    assertFalse(successBlock, TemplateEngineTests.SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with to max length parent name.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlockParentMaxLength2() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(TemplateEngineTests.MAX_VARNAME, TemplateEngineTests.VARIABLE1);
    assertFalse(successBlock, TemplateEngineTests.SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with max length varname.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlockVarnameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.MAX_VARNAME, ""); //$NON-NLS-1$
    assertFalse(successBlock, TemplateEngineTests.SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with max length name.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlockNameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE_1234, TemplateEngineTests.MAX_VARNAME);
    assertFalse(successBlock, TemplateEngineTests.SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test parsing without append.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseNonAppend() throws IOException
   {
    // tag::NonAppendBlock[]
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    /* String parseResult = */ engine.parse(TemplateEngineTests.BLK1_BLK, TemplateEngineTests.BLK1, false);
    /* String parseResult = */ engine.parse(TemplateEngineTests.BLK1_BLK, TemplateEngineTests.BLK1, false);
    // end::NonAppendBlock[]
    assertEquals("\n789\nTEST2\nabc\n", engine.getVar(TemplateEngineTests.BLK1_BLK), TemplateEngineTests.BLOCK_VALUE_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test empty parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseEmpty() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse("", "", false); //$NON-NLS-1$ //$NON-NLS-2$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test empty parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseMixed() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse(TemplateEngineTests.BLK1_BLK, "", false); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test parsing with append.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseAppend() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    /* String parseResult = */ engine.parse(TemplateEngineTests.BLK1_BLK, TemplateEngineTests.BLK1, true);
    /* String parseResult = */ engine.parse(TemplateEngineTests.BLK1_BLK, TemplateEngineTests.BLK1, true);
    assertEquals("\n789\nTEST2\nabc\n\n789\nTEST2\nabc\n", engine.getVar(TemplateEngineTests.BLK1_BLK), TemplateEngineTests.BLOCK_VALUE_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test a specific parsing problem.
   *
   * Problem description for original phplib template engine:
   *
   * When using the same template-variable within a page and a block thats contained by the page,
   * then the behavior during parsing the page might be different.
   *
   * Thats because inside the subst() method a loop runs over the list of known template variables.
   *
   * When a variable has been replaced before a block (that contains the same variable) will be replaced,
   * then the variable will not be replaced within the block. When the block will be parsed first,
   * then the variable will also be replaced within the block.
   *
   * @throws IOException IO exception
   */
  @Test
  @SuppressWarnings("checkstyle:CommentsIndentation")
  /* default */ void testParseProblem() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.KEEP);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE3, new File(TemplateEngineTests.TEMPLATE3_TMPL));
    engine.setVar(TemplateEngineTests.TEST0, "000"); //$NON-NLS-1$
    engine.setVar("test1", "111"); //$NON-NLS-1$ //$NON-NLS-2$
    engine.setVar("test3", "333"); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successBlock = engine.setBlock(TemplateEngineTests.FILE3, "test2"); //$NON-NLS-1$
    // engine.parse("test2", "test2", false); // Parse block before template to have no problems! //$NON-NLS-1$ //$NON-NLS-2$
    final String output = engine.parse(TemplateEngineTests.OUTPUT, TemplateEngineTests.FILE3);
    assertAll(
      () -> assertTrue(successBlock, "Could not cut out block!"), //$NON-NLS-1$
      () -> assertEquals("000 \n111 \n \nabc {test1} def 333 ghi \n \n333 \n000 \n", output, TemplateEngineTests.OUTPUT_VALUE_NOT_AS_EXPECTED) // Buggy result, because of order problem //$NON-NLS-1$
      // () -> assertEquals("000 \n111 \n \nabc {test1} def {test3} ghi \n \n333 \n000 \n", output) // Wanted result without block parsing //$NON-NLS-1$
      // () -> assertEquals("000 \n111 \n \nabc 111 def 333 ghi \n \n333 \n000 \n", output) // Result with block parsing //$NON-NLS-1$
    );
    // logVars(engine);
    // logUndefVars(engine, "file1");
   }


  /**
   * Test parsing with to long target name.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseTargetToLong() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse(TemplateEngineTests.TO_LONG_VARNAME, TemplateEngineTests.VARIABLE1, false);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test parsing with to long varname.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseVarnameToLong() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TO_LONG_VARNAME, false);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test parsing with wrong target name.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseTargetWrong() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse(TemplateEngineTests.VARIABLE4, TemplateEngineTests.VARIABLE1, false);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test parsing with wrong varname.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseVarnameWrong() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String parseResult = */ engine.parse(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VARIABLE4, false);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test parsing with maximum target name length.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseTargetMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    final String parseResult = engine.parse(TemplateEngineTests.MAX_VARNAME, TemplateEngineTests.VARIABLE1, false);
    assertEquals(TemplateEngineTests.VALUE1, parseResult, TemplateEngineTests.PARSE_RESULT_NOT_AS_EXPECTED);
   }


  /**
   * Test parsing with maximum varname length.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testParseVarnameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.VALUE1);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.VALUE2);
    engine.setVar(TemplateEngineTests.VARIABLE3, TemplateEngineTests.VALUE3);
    /* final boolean successBlock = */ engine.setBlock(TemplateEngineTests.FILE2, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    final String parseResult = engine.parse(TemplateEngineTests.VARIABLE1, TemplateEngineTests.MAX_VARNAME, false);
    assertEquals("", parseResult, TemplateEngineTests.PARSE_RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test empty variable list.
   */
  @Test
  /* default */ void testGetVarsEmpty()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final List<String> variables = engine.getVars();
    assertTrue(variables.isEmpty(), "Variables within empty template found!"); //$NON-NLS-1$
   }


  /**
   * Test variable list.
   */
  @Test
  /* default */ void testGetVars()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TEST);
    engine.setVar(TemplateEngineTests.VARIABLE2, TemplateEngineTests.TEST);
    final List<String> variables = engine.getVars();
    assertAll(
      () -> assertTrue(!variables.isEmpty(), "No variables within template found!"), //$NON-NLS-1$
      () -> assertNotNull(variables.get(0), "No variable in list!") //$NON-NLS-1$
    );
   }


  /**
   * Test get variable with maximum long name.
   */
  @Test
  /* default */ void testGetVarMaxLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final String value = engine.getVar(TemplateEngineTests.MAX_VARNAME);
    assertEquals("", value, "Unexpected value found"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test get variable with to long name.
   */
  @Test
  /* default */ void testGetVarToLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String value = */ engine.getVar(TemplateEngineTests.TO_LONG_VARNAME);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test get variable with wrong name.
   */
  @Test
  /* default */ void testGetVarWrongName()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String value = */ engine.getVar(TemplateEngineTests.VARIABLE4);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test subst empty file.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSubstEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    final String result = engine.subst(TemplateEngineTests.FILE4);
    assertEquals("", result, "Template not empty!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test set existing block.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testSetBlockEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(TemplateEngineTests.FILE4, new File(TemplateEngineTests.TEMPLATE4_TMPL));
    final boolean successBlock = engine.setBlock(TemplateEngineTests.FILE4, TemplateEngineTests.BLK1, TemplateEngineTests.BLK1_BLK);
    assertFalse(successBlock, "Block could be cut out successfully from empty template!"); //$NON-NLS-1$
   }


  /**
   * Test read template file from classpath.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testReadFromClasspath() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(TemplateEngineTests.FILE5, new File("template5.tmpl")); //$NON-NLS-1$
    final String variableValue = engine.subst(TemplateEngineTests.FILE5);
    assertAll(
      () -> assertTrue(success, "Template file template5.tmpl could not be set!"), //$NON-NLS-1$
      () -> assertNotNull(variableValue, "Template file template5.tmpl could not be loaded from classpath") //$NON-NLS-1$
    );
   }


  /**
   * Test read template file from classpath.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testReadNonExisting() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(TemplateEngineTests.FILE0, new File(TemplateEngineTests.TEMPLATE0_TMPL));
    final String variableValue = engine.subst(TemplateEngineTests.FILE0);
    assertAll(
      () -> assertFalse(success, "Template file template0.tmpl could be set!"), //$NON-NLS-1$
      () -> assertEquals("", variableValue, "Template file template0.tmpl could be loaded from classpath") //$NON-NLS-1$ //$NON-NLS-2$
    );
   }


  /**
   * Test unset variable.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testUnsetVar() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TEST);
    engine.unsetVar(TemplateEngineTests.VARIABLE1);
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertFalse(undefinedVars.isEmpty(), TemplateEngineTests.NO_UNDEFINED_VARIABLE_S_FOUND),
      () -> assertEquals(TemplateEngineTests.VARIABLE1, undefinedVars.get(0), TemplateEngineTests.UNDEFINED_VARIABLE_NOT_AS_EXPECTED)
    );
   }


  /**
   * Test unset empty variable.
   */
  @Test
  /* default */ void testUnsetVarEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.unsetVar(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test unset variable with maximum name length.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testUnsetVarMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE8_TMPL));
    engine.setVar(TemplateEngineTests.MAX_VARNAME, TemplateEngineTests.TEST);
    engine.unsetVar(TemplateEngineTests.MAX_VARNAME);
    final List<String> undefinedVars = engine.getUndefined(TemplateEngineTests.FILE1);
    assertAll(
      () -> assertFalse(undefinedVars.isEmpty(), TemplateEngineTests.NO_UNDEFINED_VARIABLE_S_FOUND),
      () -> assertEquals(TemplateEngineTests.MAX_VARNAME, undefinedVars.get(0), TemplateEngineTests.UNDEFINED_VARIABLE_NOT_AS_EXPECTED)
    );
   }


  /**
   * Test unset variable with to long name.
   */
  @Test
  /* default */ void testUnsetVarToLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.unsetVar(TemplateEngineTests.TO_LONG_VARNAME);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test unset variable with wrong name.
   */
  @Test
  /* default */ void testUnsetVarWrongName()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.unsetVar(TemplateEngineTests.VARIABLE4);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File(TemplateEngineTests.TEMPLATE1_TMPL));
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE2, new File(TemplateEngineTests.TEMPLATE2_TMPL));
    engine.setVar(TemplateEngineTests.VARIABLE1, TemplateEngineTests.TEST);
    final String string = engine.toString();
    assertEquals("TemplateEngine[unknowns=REMOVE, vManager=VariableManager[vars=[variable1]], fManager=FileManager[files=Optional[template2.tmpl, template1.tmpl]], bManager=BlockManager[vars=[variable1]]]", string, "toString() result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test newInstance from file.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceFile1() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(new File(TemplateEngineTests.TEMPLATE1_TMPL));
    /* String result = */ engine.subst(TemplateEngineTests.TEMPLATE);
    final String value = engine.getVar(TemplateEngineTests.TEMPLATE);
    assertAll(
      () -> assertNotNull(value, TemplateEngineTests.NO_TEMPLATE_FOUND),
      () -> assertFalse(value.isEmpty(), TemplateEngineTests.NO_TEMPLATE_FOUND)
    );
   }


  /**
   * Test newInstance from non existing file.
   */
  @Test
  /* default */ void testNewInstanceFileNonExisting()
   {
    assertThrows(FileNotFoundException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(new File(TemplateEngineTests.TEMPLATE0_TMPL));
     }, "File not found exception expected"
    );
   }


  /**
   * Test newInstance from directory.
   */
  @Test
  /* default */ void testNewInstanceFileFromDirectory()
   {
    final File file = new File("target/test-classes/templates/");
    assertThrows(AssertionError.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(file);
     }, TemplateEngineTests.ASSERTION_ERROR_EXPECTED
    );
   }


  /**
   * Test newInstance from File with null.
   */
  @Test
  /* default */ void testNewInstanceFileNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((File)null);
     }, TemplateEngineTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test newInstance with a to large file.
   */
  @Test
  /* default */ void testNewInstanceFileToLarge()
   {
    assertThrows(IOException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(new File(TemplateEngineTests.TEMPLATE10_TMPL));
     }, "IO exception expected"
    );
   }


  /**
   * Test newInstance with maximum size file.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceFileMaxSize() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(new File(TemplateEngineTests.TEMPLATE9_TMPL));
    assertNotNull(engine, TemplateEngineTests.NEW_INSTANCE_RESULT_NOT_AS_EXPECTED);
   }


  /**
   * Test newInstance from InputStream with null.
   */
  @Test
  /* default */ void testNewInstanceInputStreamNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((InputStream)null);
     }, TemplateEngineTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test newInstance from InputStream.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceInputStream() throws IOException
   {
    try (InputStream stream = this.getClass().getResourceAsStream("/template5.tmpl")) //$NON-NLS-1$
     {
      final TemplateEngine engine = TemplateEngine.newInstance(stream);
      final String value = engine.getVar(TemplateEngineTests.TEMPLATE);
      assertAll(
        () -> assertNotNull(value, TemplateEngineTests.NO_TEMPLATE_FOUND),
        () -> assertFalse(value.isEmpty(), TemplateEngineTests.NO_TEMPLATE_FOUND)
      );
     }
   }


  /**
   * Test newInstance from empty InputStream.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceInputStreamEmpty() throws IOException
   {
    try (InputStream stream = this.getClass().getResourceAsStream("/template6.tmpl")) //$NON-NLS-1$
     {
      assertThrows(IllegalStateException.class, () ->
       {
        /* final TemplateEngine engine = */ TemplateEngine.newInstance(stream);
       }, TemplateEngineTests.ILLEGAL_STATE_EXCEPTION_EXPECTED
      );
     }
   }


  /**
   * Test newInstance from String with null.
   */
  @Test
  /* default */ void testNewInstanceStringNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((String)null);
     }, TemplateEngineTests.NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test newInstance from String with empty string.
   */
  @Test
  /* default */ void testNewInstanceStringEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test newInstance from String.
   */
  @Test
  /* default */ void testNewInstanceString()
   {
    final TemplateEngine engine = TemplateEngine.newInstance("123\r\n{variable1}\r\n456\r\n"); //$NON-NLS-1$
    final String value = engine.getVar(TemplateEngineTests.TEMPLATE);
    assertAll(
      () -> assertNotNull(value, TemplateEngineTests.NO_TEMPLATE_FOUND),
      () -> assertFalse(value.isEmpty(), TemplateEngineTests.NO_TEMPLATE_FOUND)
    );
   }


  /**
   * Read String from File.
   *
   * @param file File to read from
   * @return File content as String
   * @throws IOException IO exception
   */
  private static String readStringFromFile(final File file) throws IOException
   {
    return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
   }


  /**
   * Test newInstance from String with to large string.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceStringToLarge() throws IOException
   {
    final String templ = readStringFromFile(new File(TemplateEngineTests.TEMPLATE10_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(templ);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test newInstance from String with max size string.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceStringMaxSize() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(readStringFromFile(new File(TemplateEngineTests.TEMPLATE9_TMPL)));
    assertNotNull(engine, TemplateEngineTests.NEW_INSTANCE_RESULT_NOT_AS_EXPECTED);
   }


  /**
   * Test newInstance from TemplateEngine copy.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testNewInstanceCopy() throws IOException
   {
    final TemplateEngine engine1 = TemplateEngine.newInstance(new File(TemplateEngineTests.TEMPLATE1_TMPL));
    /* String result = */ engine1.subst(TemplateEngineTests.TEMPLATE);
    final TemplateEngine engine2 = TemplateEngine.newInstance(engine1);
    final String value2 = engine2.getVar(TemplateEngineTests.TEMPLATE);
    assertAll(
      () -> assertNotNull(value2, TemplateEngineTests.NO_TEMPLATE_FOUND),
      () -> assertFalse(value2.isEmpty(), TemplateEngineTests.NO_TEMPLATE_FOUND)
    );
   }


  /**
   * Test copy constructor.
   */
  @Test
  /* default */ void testCopyConstructor()
   {
    final TemplateEngine engine1 = new TemplateEngine();
    final TemplateEngine engine2 = new TemplateEngine(engine1);
    assertNotNull(engine2, "Copy constructor failed!"); //$NON-NLS-1$
   }


  /**
   * Test get empty variable with value.
   */
  @Test
  /* default */ void testGetVarEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final String value = */ engine.getVar(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test empty finish.
   */
  @Test
  /* default */ void testFinishEmpty()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertThrows(IllegalArgumentException.class, () ->
     {
      engine.finish(""); //$NON-NLS-1$
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test finish with to large template.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testFinishToLarge() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final String templ = readStringFromFile(new File(TemplateEngineTests.TEMPLATE10_TMPL));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* String result = */ engine.finish(templ);
     }, TemplateEngineTests.ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test finish with maximum template size.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testFinishMaxSize() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final String result = engine.finish(readStringFromFile(new File(TemplateEngineTests.TEMPLATE9_TMPL)));
    assertNotNull(result, "finish result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test two { after each other within template.
   *
   * @throws IOException IO exception
   */
  @Test
  /* default */ void testDoubleOpenCurlyBrace() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(TemplateEngineTests.FILE1, new File("target/test-classes/templates/template7.tmpl")); //$NON-NLS-1$
    engine.setVar(TemplateEngineTests.VARIABLE1, ""); //$NON-NLS-1$
    final String variableValue = engine.subst(TemplateEngineTests.FILE1);
    assertEquals("123\n{\n456\n", variableValue, TemplateEngineTests.VARIABLE_VALUE_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final TemplateEngine tmpl1 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl2 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl3 = new TemplateEngine(HandleUndefined.KEEP);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(tmpl1.hashCode(), tmpl2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(tmpl1.hashCode(), tmpl3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   *
   * @throws IOException IO exception
   */
  @Test
  @SuppressFBWarnings("EC_NULL_ARG")
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals() throws IOException
   {
    final TemplateEngine tmpl1 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl2 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl3 = new TemplateEngine(HandleUndefined.KEEP);
    final TemplateEngine tmpl4 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl5 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl6 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl7 = new TemplateEngine(HandleUndefined.REMOVE);
    tmpl5.setVar("key", "value"); //$NON-NLS-1$ //$NON-NLS-2$
    /* boolean success = */ tmpl6.setFile("file", new File(TemplateEngineTests.TEMPLATE1_TMPL)); //$NON-NLS-1$
    tmpl7.setVar(PARENT, "before<!-- BEGIN blktest -->content<!-- END blktest -->after");
    /* final boolean result = */ tmpl7.setBlock(PARENT, "blktest");
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl1), "TemplateEngine11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl2), "TemplateEngine12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl2.equals(tmpl1), "TemplateEngine21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl2.equals(tmpl4), "TemplateEngine24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl4), "TemplateEngine14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(tmpl3), "TemplateEngine13 are equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl3.equals(tmpl1), "TemplateEngine31 are equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(null), "TemplateEngine10 is equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(tmpl5), "TemplateEngine15 is equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(tmpl6), "TemplateEngine16 is equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(tmpl7), "TemplateEngine17 is equal") //$NON-NLS-1$
    );
   }

 }
