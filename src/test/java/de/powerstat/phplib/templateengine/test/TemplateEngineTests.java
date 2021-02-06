/*
 * Copyright (C) 2019 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.phplib.templateengine.TemplateEngine;
import de.powerstat.phplib.templateengine.TemplateEngine.HandleUndefined;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * TemplateEngine tests.
 */
@SuppressFBWarnings({"CE_CLASS_ENVY", "EC_NULL_ARG"})
public final class TemplateEngineTests
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
  private static final String SET_BLOCK_NOT_AS_EXPECTED = "SetBlock not as expected";


  /**
   * Default constructor.
   */
  public TemplateEngineTests()
   {
    super();
   }


  /**
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


  /**
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
  public void defaultConstructor()
   {
    final TemplateEngine engine = new TemplateEngine();
    assertNotNull(engine, "Default constructor failed!"); //$NON-NLS-1$
   }


  /**
   * Test set template file.
   */
  @Test
  public void setFile()
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    assertTrue(success, "Template file template1.tmpl could not be loaded!"); //$NON-NLS-1$
   }


  /**
   * Test set template file with max length.
   */
  @Test
  public void setFileMaxLength()
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile(MAX_VARNAME, new File(TEMPLATE1_TMPL));
    assertTrue(success, "Template file template1.tmpl could not be loaded!"); //$NON-NLS-1$
   }


  /**
   * Test set template file.
   */
  @Test
  public void setFileEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile("", new File(TEMPLATE1_TMPL)); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set template file with to long filename.
   */
  @Test
  public void setFileToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(TO_LONG_VARNAME, new File(TEMPLATE1_TMPL));
     }
    );
   }


  /**
   * Test set template file with wrong filename.
   */
  @Test
  public void setFileWrongname()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile("file~1", new File(TEMPLATE1_TMPL)); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set template file with to large file.
   */
  @Test
  public void setFileToLarge()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(VARIABLE1, new File(TEMPLATE10_TMPL));
     }
    );
   }


  /**
   * Test read template file.
   *
   * @throws IOException IO exception
   */
  @Test
  public void subst() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    final String variableValue = engine.subst(FILE1);
    if (LOGGER.isDebugEnabled())
     {
      LOGGER.debug("file1 = " + variableValue); //$NON-NLS-1$
     }
    assertEquals("123\n{variable1}\n456\n", variableValue, VARIABLE_VALUE_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test read empty template file.
   */
  @Test
  public void substEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
      /* final String variableValue = */ engine.subst(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test subst with to long varname.
   */
  @Test
  public void substToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
      /* final String variableValue = */ engine.subst(TO_LONG_VARNAME);
     }
    );
   }


  /**
   * Test subst with wrong varname.
   */
  @Test
  public void substWrongName()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
      /* final String variableValue = */ engine.subst(VARIABLE4);
     }
    );
   }


  /**
   * Test subst with max varname length varname.
   *
   * @throws IOException IO exception
   */
  @Test
  public void substMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    final String variableValue = engine.subst(MAX_VARNAME);
    assertEquals("", variableValue, "Subst result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get undefined variables from template.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getUndefined() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* final String variableValue = */ engine.subst(FILE1);
    final List<String> undefinedVars = engine.getUndefined(FILE1);
    assertAll(
      () -> assertEquals(1, undefinedVars.size(), "Found more or less undefined variables"), //$NON-NLS-1$
      () -> assertEquals(VARIABLE1, undefinedVars.get(0), "Not found expected undefined variable") //$NON-NLS-1$
    );
   }


  /**
   * Get undefined variables from empty template.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getUndefinedFromEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
    // /* final String variableValue = */ engine.subst("file4"); //$NON-NLS-1$
    final List<String> undefinedVars = engine.getUndefined(FILE4);
    assertEquals(0, undefinedVars.size(), "Found undefined variables"); //$NON-NLS-1$
   }


  /**
   * Get empty undefined variables.
   */
  @Test
  public void getUndefinedEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
      // /* final String variableValue = */ engine.subst("file4"); //$NON-NLS-1$
      /* final List<String> undefinedVars = */ engine.getUndefined(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Get undefined variables with to long varname.
   */
  @Test
  public void getUndefinedToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
      // /* final String variableValue = */ engine.subst("file4"); //$NON-NLS-1$
      /* final List<String> undefinedVars = */ engine.getUndefined(TO_LONG_VARNAME);
     }
    );
   }


  /**
   * Get undefined variables with wrong varname.
   */
  @Test
  public void getUndefinedWrongName()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final boolean success = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
      // /* final String variableValue = */ engine.subst("file4"); //$NON-NLS-1$
      /* final List<String> undefinedVars = */ engine.getUndefined(VARIABLE4);
     }
    );
   }


  /**
   * Get undefined variables with maximum varname length.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getUndefinedMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
    // /* final String variableValue = */ engine.subst("file4"); //$NON-NLS-1$
    final List<String> undefinedVars = engine.getUndefined(MAX_VARNAME);
    assertEquals(new ArrayList<>(), undefinedVars, "GetUndefined result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test set variable as empty.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setVarEmpty() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    engine.setVar(VARIABLE1);
    /* final String variableValue = */ engine.subst(FILE1);
    final List<String> undefinedVars = engine.getUndefined(FILE1);
    assertAll(
      () -> assertTrue(undefinedVars.isEmpty(), "Undefined variable(s) found!"), //$NON-NLS-1$
      () -> assertTrue(engine.getVar(VARIABLE1).isEmpty(), "variable1 is not empty!") //$NON-NLS-1$
    );
   }


  /**
   * Test set variable with value.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setVar() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    engine.setVar(VARIABLE1, TEST);
    /* final String variableValue = */ engine.subst(FILE1);
    final List<String> undefinedVars = engine.getUndefined(FILE1);
    assertAll(
      () -> assertTrue(undefinedVars.isEmpty(), "Undefined variable(s) found!"), //$NON-NLS-1$
      () -> assertEquals(TEST, engine.getVar(VARIABLE1), VARIABLE_VALUE_NOT_AS_EXPECTED)
    );
   }


  /**
   * Test set empty variable with value.
   */
  @Test
  public void setEmptyVar()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.setVar("", TEST); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set variable with maximum long name.
   */
  @Test
  public void setVarMaxLong()
   {
    final TemplateEngine engine = new TemplateEngine();
    engine.setVar(MAX_VARNAME, TEST);
    assertEquals(TEST, engine.getVar(MAX_VARNAME), VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * Test set variable with to long name.
   */
  @Test
  public void setVarToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.setVar(TO_LONG_VARNAME, TEST);
     }
    );
   }


  /**
   * Test set variable with wrong name.
   */
  @Test
  public void setVarWrongName()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.setVar(VARIABLE4, TEST);
     }
    );
   }


  /**
   * Test set variable with to large value.
   */
  @Test
  public void setVarToLarge()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.setVar(VARIABLE1, readStringFromFile(new File(TEMPLATE10_TMPL)));
     }
    );
   }


  /**
   * Test set variable with maximum value size.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setVarMaxLarge() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    engine.setVar(VARIABLE1, readStringFromFile(new File(TEMPLATE9_TMPL)));
    assertNotNull(engine.getVar(VARIABLE1), VARIABLE_VALUE_NOT_AS_EXPECTED);
   }


  /**
   * Test get removed variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getRemoved() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* String substResult = */ engine.subst(FILE1);
    final String parseResult = engine.parse(OUTPUT, FILE1);
    final String output = engine.get(OUTPUT);
    assertAll(
      () -> assertNotNull(parseResult, "Parse result is null!"), //$NON-NLS-1$
      () -> assertEquals("123\n\n456\n", output, "Output not as expected") //$NON-NLS-1$ //$NON-NLS-2$
    );
   }


  /**
   * Test get keep variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getKeep() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.KEEP);
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* String substResult = */ engine.subst(FILE1);
    /* String parseResult = */ engine.parse(OUTPUT, FILE1);
    final String output = engine.get(OUTPUT);
    assertEquals("123\n{variable1}\n456\n", output, "Output not as exptected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test get replace with comment variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  public void getComment() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* String subsrResult = */ engine.subst(FILE1);
    /* String parseResult = */ engine.parse(OUTPUT, FILE1);
    final String output = engine.get(OUTPUT);
    assertEquals("123\n<!-- Template variable 'variable1' undefined -->\n456\n", output, "Output not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test set non existing block.
   */
  @Test
  public void setBlockFailure()
   {
    assertThrows(IllegalStateException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      /* final boolean successBlock = */ engine.setBlock(FILE2, "blk2"); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set empty block name.
   */
  @Test
  public void setBlockEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock("", ""); //$NON-NLS-1$ //$NON-NLS-2$
     }
    );
   }


  /**
   * Test set existing block.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setBlock() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    /* String substResult = */ engine.subst(FILE2);
    final boolean successBlock = engine.setBlock(FILE2, BLK1, BLK1_BLK);
    final String block = engine.getVar(BLK1);
    assertAll(
      () -> assertTrue(successBlock, "Block could not be cut out successfully!"), //$NON-NLS-1$
      () -> assertEquals("\n789\n{variable2}\nabc\n", block, "Block value not as expected") //$NON-NLS-1$ //$NON-NLS-2$
    );
   }


  /**
   * Test set block with to long parent name.
   */
  @Test
  public void setBlockParentToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(TO_LONG_VARNAME, VARIABLE1, ""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with to long varname.
   */
  @Test
  public void setBlockVarnameToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(VARIABLE1, TO_LONG_VARNAME, ""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with to long name.
   */
  @Test
  public void setBlockNameToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(VARIABLE1, "1234", TO_LONG_VARNAME); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with to wrong parent name.
   */
  @Test
  public void setBlockParentWrong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(VARIABLE4, VARIABLE1, ""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with wrong varname.
   */
  @Test
  public void setBlockVarnameWrong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(VARIABLE1, VARIABLE4, ""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with wrong name.
   */
  @Test
  public void setBlockNameWrong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successBlock = */ engine.setBlock(VARIABLE1, "1234", VARIABLE4); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test set block with to max length parent name.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setBlockParentMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(MAX_VARNAME, VARIABLE1, ""); //$NON-NLS-1$
    assertFalse(successBlock, SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with to max length parent name.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setBlockParentMaxLength2() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(MAX_VARNAME, VARIABLE1);
    assertFalse(successBlock, SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with max length varname.
   * @throws IOException IO exception
   */
  @Test
  public void setBlockVarnameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(VARIABLE1, MAX_VARNAME, ""); //$NON-NLS-1$
    assertFalse(successBlock, SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test set block with max length name.
   * @throws IOException IO exception
   */
  @Test
  public void setBlockNameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final boolean successBlock = engine.setBlock(VARIABLE1, "1234", MAX_VARNAME); //$NON-NLS-1$
    assertFalse(successBlock, SET_BLOCK_NOT_AS_EXPECTED);
   }


  /**
   * Test parsing without append.
   *
   * @throws IOException IO exception
   */
  @Test
  public void parseNonAppend() throws IOException
   {
    // tag::NonAppendBlock[]
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    /* String substResult = */ engine.subst(FILE2);
    engine.setVar(VARIABLE1, VALUE1);
    engine.setVar(VARIABLE2, VALUE2);
    engine.setVar(VARIABLE3, VALUE3);
    /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
    /* String parseResult = */ engine.parse(BLK1_BLK, BLK1, false);
    /* String parseResult = */ engine.parse(BLK1_BLK, BLK1, false);
    // end::NonAppendBlock[]
    assertEquals("\n789\nTEST2\nabc\n", engine.getVar(BLK1_BLK), "Block value not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test empty parsing.
   */
  @Test
  public void parseEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      engine.setVar(VARIABLE1, VALUE1);
      engine.setVar(VARIABLE2, VALUE2);
      engine.setVar(VARIABLE3, VALUE3);
      /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
      /* String parseResult = */ engine.parse("", "", false); //$NON-NLS-1$ //$NON-NLS-2$
     }
    );
   }


  /**
   * Test parsing with append.
   *
   * @throws IOException IO exception
   */
  @Test
  public void parseAppend() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    /* String substResult = */ engine.subst(FILE2);
    engine.setVar(VARIABLE1, VALUE1);
    engine.setVar(VARIABLE2, VALUE2);
    engine.setVar(VARIABLE3, VALUE3);
    /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
    /* String parseResult = */ engine.parse(BLK1_BLK, BLK1, true);
    /* String parseResult = */ engine.parse(BLK1_BLK, BLK1, true);
    assertEquals("\n789\nTEST2\nabc\n\n789\nTEST2\nabc\n", engine.getVar(BLK1_BLK), "Block value not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
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
  public void parseProblem() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.KEEP);
    /* final boolean successFile = */ engine.setFile(FILE3, new File("target/test-classes/templates/template3.tmpl")); //$NON-NLS-1$
    /* String substResult = */ engine.subst(FILE3);
    engine.setVar("test0", "000"); //$NON-NLS-1$ //$NON-NLS-2$
    engine.setVar("test1", "111"); //$NON-NLS-1$ //$NON-NLS-2$
    engine.setVar("test3", "333"); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successBlock = engine.setBlock(FILE3, "test2"); //$NON-NLS-1$
    // engine.parse("test2", "test2", false); // Parse block before template to have no problems! //$NON-NLS-1$ //$NON-NLS-2$
    final String output = engine.parse(OUTPUT, FILE3);
    assertAll(
      () -> assertTrue(successBlock, "Could not cut out block!"), //$NON-NLS-1$
      () -> assertEquals("000 \n111 \n \nabc {test1} def 333 ghi \n \n333 \n000 \n", output, "Output value not as expected") // Buggy result, because of order problem //$NON-NLS-1$ //$NON-NLS-2$
      // () -> assertEquals("000 \n111 \n \nabc {test1} def {test3} ghi \n \n333 \n000 \n", output) // Wanted result without block parsing //$NON-NLS-1$
      // () -> assertEquals("000 \n111 \n \nabc 111 def 333 ghi \n \n333 \n000 \n", output) // Result with block parsing //$NON-NLS-1$
    );

    // logVars(engine);
    // logUndefVars(engine, "file1");
   }


  /**
   * Test parsing with to long target name.
   */
  @Test
  public void parseTargetToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      engine.setVar(VARIABLE1, VALUE1);
      engine.setVar(VARIABLE2, VALUE2);
      engine.setVar(VARIABLE3, VALUE3);
      /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
      /* String parseResult = */ engine.parse(TO_LONG_VARNAME, VARIABLE1, false);
     }
    );
   }


  /**
   * Test parsing with to long varname.
   */
  @Test
  public void parseVarnameToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      engine.setVar(VARIABLE1, VALUE1);
      engine.setVar(VARIABLE2, VALUE2);
      engine.setVar(VARIABLE3, VALUE3);
      /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
      /* String parseResult = */ engine.parse(VARIABLE1, TO_LONG_VARNAME, false);
     }
    );
   }


  /**
   * Test parsing with wrong target name.
   */
  @Test
  public void parseTargetWrong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      engine.setVar(VARIABLE1, VALUE1);
      engine.setVar(VARIABLE2, VALUE2);
      engine.setVar(VARIABLE3, VALUE3);
      /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
      /* String parseResult = */ engine.parse(VARIABLE4, VARIABLE1, false);
     }
    );
   }


  /**
   * Test parsing with wrong varname.
   */
  @Test
  public void parseVarnameWrong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
      /* String substResult = */ engine.subst(FILE2);
      engine.setVar(VARIABLE1, VALUE1);
      engine.setVar(VARIABLE2, VALUE2);
      engine.setVar(VARIABLE3, VALUE3);
      /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
      /* String parseResult = */ engine.parse(VARIABLE1, VARIABLE4, false);
     }
    );
   }


  /**
   * Test parsing with maximum target name length.
   *
   * @throws IOException IO exception
   */
  @Test
  public void parseTargetMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    /* String substResult = */ engine.subst(FILE2);
    engine.setVar(VARIABLE1, VALUE1);
    engine.setVar(VARIABLE2, VALUE2);
    engine.setVar(VARIABLE3, VALUE3);
    /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
    final String parseResult = engine.parse(MAX_VARNAME, VARIABLE1, false);
    assertEquals(VALUE1, parseResult, "Parse result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test parsing with maximum varname length.
   *
   * @throws IOException IO exception
   */
  @Test
  public void parseVarnameMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    /* String substResult = */ engine.subst(FILE2);
    engine.setVar(VARIABLE1, VALUE1);
    engine.setVar(VARIABLE2, VALUE2);
    engine.setVar(VARIABLE3, VALUE3);
    /* final boolean successBlock = */ engine.setBlock(FILE2, BLK1, BLK1_BLK);
    final String parseResult = engine.parse(VARIABLE1, MAX_VARNAME, false);
    assertEquals("", parseResult, "Parse result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test empty variable list.
   */
  @Test
  public void getVarsEmpty()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final List<String> variables = engine.getVars();
    assertTrue(variables.isEmpty(), "Variables within empty template found!"); //$NON-NLS-1$
   }


  /**
   * Test variable list.
   */
  @Test
  public void getVars()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    engine.setVar(VARIABLE1, TEST);
    engine.setVar(VARIABLE2, TEST);
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
  public void getVarMaxLong()
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    final String value = engine.getVar(MAX_VARNAME);
    assertEquals("", value, "Unexpected value found"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test get variable with to long name.
   */
  @Test
  public void getVarToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final String value = */ engine.getVar(TO_LONG_VARNAME);
     }
    );
   }


  /**
   * Test get variable with wrong name.
   */
  @Test
  public void getVarWrongName()
   {
    assertThrows(IllegalArgumentException.class, () ->
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
      /* final String value = */ engine.getVar(VARIABLE4);
     }
    );
   }


  /**
   * Test subst empty file.
   *
   * @throws IOException IO exception
   */
  @Test
  public void substEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
    final String result = engine.subst(FILE4);
    assertEquals("", result, "Template not empty!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test set existing block.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setBlockEmptyFile() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine(HandleUndefined.COMMENT);
    /* final boolean successFile = */ engine.setFile(FILE4, new File(TEMPLATE4_TMPL));
    // /* final String result = */ engine.subst("file4"); //$NON-NLS-1$
    final boolean successBlock = engine.setBlock(FILE4, BLK1, BLK1_BLK);
    assertFalse(successBlock, "Block could be cut out successfully from empty template!"); //$NON-NLS-1$
     }


  /**
   * Test get keep variable parsing.
   *
   * @throws IOException IO exception
   */
  @Test
  public void setUnknowns() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    engine.setUnknowns(HandleUndefined.KEEP);
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* String substResult = */ engine.subst(FILE1);
    /* String parseResult = */ engine.parse(OUTPUT, FILE1);
    final String output = engine.get(OUTPUT);
    assertEquals("123\n{variable1}\n456\n", output, "Output value not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test read template file from classpath.
   *
   * @throws IOException IO exception
   */
  @Test
  public void readFromClasspath() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile("file5", new File("template5.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    final String variableValue = engine.subst("file5"); //$NON-NLS-1$
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
  public void readNonExisting() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final boolean success = engine.setFile("file0", new File("template0.tmpl")); //$NON-NLS-1$ //$NON-NLS-2$
    final String variableValue = engine.subst("file0"); //$NON-NLS-1$
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
  public void unsetVar() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    engine.setVar(VARIABLE1, TEST);
    engine.unsetVar(VARIABLE1);
    /* final String variableValue = */ engine.subst(FILE1);
    final List<String> undefinedVars = engine.getUndefined(FILE1);
    assertAll(
      () -> assertFalse(undefinedVars.isEmpty(), "No undefined variable(s) found!"), //$NON-NLS-1$
      () -> assertEquals(VARIABLE1, undefinedVars.get(0), "Undefined variable not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test unset empty variable.
   */
  @Test
  public void unsetVarEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.unsetVar(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test unset variable with maximum name length.
   *
   * @throws IOException IO exception
   */
  @Test
  public void unsetVarMaxLength() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE8_TMPL));
    engine.setVar(MAX_VARNAME, TEST);
    engine.unsetVar(MAX_VARNAME);
    /* final String variableValue = */ engine.subst(FILE1);
    final List<String> undefinedVars = engine.getUndefined(FILE1);
    assertAll(
      () -> assertFalse(undefinedVars.isEmpty(), "No undefined variable(s) found!"), //$NON-NLS-1$
      () -> assertEquals(MAX_VARNAME, undefinedVars.get(0), "Undefined variable not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test unset variable with to long name.
   */
  @Test
  public void unsetVarToLong()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.unsetVar(TO_LONG_VARNAME);
     }
    );
   }


  /**
   * Test unset variable with wrong name.
   */
  @Test
  public void unsetVarWrongName()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.unsetVar(VARIABLE4);
     }
    );
   }


  /**
   * Test toString.
   */
  @Test
  public void testToString()
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File(TEMPLATE1_TMPL));
    /* final boolean success = */ engine.setFile(FILE2, new File(TEMPLATE2_TMPL));
    engine.setVar(VARIABLE1, TEST);
    final String string = engine.toString();
    assertEquals("TemplateEngine[unknowns=REMOVE, files=Optional[template2.tmpl, template1.tmpl], vars=[variable1]]", string, "toString() result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test newInstance from file.
   *
   * @throws IOException IO exception
   */
  @Test
  public void newInstanceFile1() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(new File(TEMPLATE1_TMPL));
    /* String result = */ engine.subst(TEMPLATE);
    final String value = engine.getVar(TEMPLATE);
    assertAll(
      () -> assertNotNull(value, NO_TEMPLATE_FOUND),
      () -> assertFalse(value.isEmpty(), NO_TEMPLATE_FOUND)
    );
   }


  /**
   * Test newInstance from non existing file.
   */
  @Test
  public void newInstanceFileNonExisting()
   {
    assertThrows(FileNotFoundException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(new File("template0.tmpl")); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test newInstance from directory.
   */
  @Test
  public void newInstanceFileFromDirectory()
   {
    assertThrows(AssertionError.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(new File("target/test-classes/templates/")); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test newInstance from File with null.
   */
  @Test
  public void newInstanceFileNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((File)null);
     }
    );
   }


  /**
   * Test newInstance with a to large file.
   */
  @Test
  public void newInstanceFileToLarge()
   {
    assertThrows(IOException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(new File(TEMPLATE10_TMPL));
     }
    );
   }


  /**
   * Test newInstance with maximum size file.
   *
   * @throws IOException IO exception
   */
  @Test
  public void newInstanceFileMaxSize() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(new File(TEMPLATE9_TMPL));
    assertNotNull(engine, "newInstance result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test newInstance from InputStream with null.
   */
  @Test
  public void newInstanceInputStreamNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((InputStream)null);
     }
    );
   }


  /**
   * Test newInstance from InputStream.
   *
   * @throws IOException IO exception
   */
  @Test
  public void newInstanceInputStream() throws IOException
   {
    try (InputStream stream = this.getClass().getResourceAsStream("/template5.tmpl")) //$NON-NLS-1$
     {
      final TemplateEngine engine = TemplateEngine.newInstance(stream);
      final String value = engine.getVar(TEMPLATE);
      assertAll(
        () -> assertNotNull(value, NO_TEMPLATE_FOUND),
        () -> assertFalse(value.isEmpty(), NO_TEMPLATE_FOUND)
      );
     }
   }


  /**
   * Test newInstance from empty InputStream.
   */
  @Test
  public void newInstanceInputStreamEmpty()
   {
    assertThrows(IllegalStateException.class, () ->
     {
      try (InputStream stream = this.getClass().getResourceAsStream("/template6.tmpl")) //$NON-NLS-1$
       {
        final TemplateEngine engine = TemplateEngine.newInstance(stream);
        /* final String value = */ engine.getVar(TEMPLATE);
       }
     }
    );
   }


  /**
   * Test newInstance from String with null.
   */
  @Test
  public void newInstanceStringNull()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance((String)null);
     }
    );
   }


  /**
   * Test newInstance from String with empty string.
   */
  @Test
  public void newInstanceStringEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test newInstance from String.
   */
  @Test
  public void newInstanceString()
   {
    final TemplateEngine engine = TemplateEngine.newInstance("123\r\n{variable1}\r\n456\r\n"); //$NON-NLS-1$
    final String value = engine.getVar(TEMPLATE);
    assertAll(
      () -> assertNotNull(value, NO_TEMPLATE_FOUND),
      () -> assertFalse(value.isEmpty(), NO_TEMPLATE_FOUND)
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
   */
  @Test
  public void newInstanceStringToLarge()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final TemplateEngine engine = */ TemplateEngine.newInstance(readStringFromFile(new File(TEMPLATE10_TMPL)));
     }
    );
   }


  /**
   * Test newInstance from String with max size string.
   *
   * @throws IOException IO exception
   */
  @Test
  public void newInstanceStringMaxSize() throws IOException
   {
    final TemplateEngine engine = TemplateEngine.newInstance(readStringFromFile(new File(TEMPLATE9_TMPL)));
    assertNotNull(engine, "newInstance result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test newInstance from TemplateEngine copy.
   *
   * @throws IOException IO exception
   */
  @Test
  public void newInstanceCopy() throws IOException
   {
    final TemplateEngine engine1 = TemplateEngine.newInstance(new File(TEMPLATE1_TMPL));
    /* String result = */ engine1.subst(TEMPLATE);
    final TemplateEngine engine2 = TemplateEngine.newInstance(engine1);
    final String value2 = engine2.getVar(TEMPLATE);
    assertAll(
      () -> assertNotNull(value2, NO_TEMPLATE_FOUND),
      () -> assertFalse(value2.isEmpty(), NO_TEMPLATE_FOUND)
    );
   }


  /**
   * Test copy constructor.
   */
  @Test
  public void copyConstructor()
   {
    final TemplateEngine engine1 = new TemplateEngine();
    final TemplateEngine engine2 = new TemplateEngine(engine1);
    assertNotNull(engine2, "Copy constructor failed!"); //$NON-NLS-1$
   }


  /**
   * Test get empty variable with value.
   */
  @Test
  public void getVarEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* final String value = */ engine.getVar(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test empty finish.
   */
  @Test
  public void finishEmpty()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      engine.finish(""); //$NON-NLS-1$
     }
    );
   }


  /**
   * Test finish with to large template.
   */
  @Test
  public void finishToLarge()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      final TemplateEngine engine = new TemplateEngine();
      /* String result = */ engine.finish(readStringFromFile(new File(TEMPLATE10_TMPL)));
     }
    );
   }


  /**
   * Test finish with maximum template size.
   *
   * @throws IOException IO exception
   */
  @Test
  public void finishMaxSize() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    final String result = engine.finish(readStringFromFile(new File(TEMPLATE9_TMPL)));
    assertNotNull(result, "finish result not as expected"); //$NON-NLS-1$
   }


  /**
   * Test two { after each other within template.
   *
   * @throws IOException IO exception
   */
  @Test
  public void doubleOpenCurlyBrace() throws IOException
   {
    final TemplateEngine engine = new TemplateEngine();
    /* final boolean success = */ engine.setFile(FILE1, new File("target/test-classes/templates/template7.tmpl")); //$NON-NLS-1$
    engine.setVar(VARIABLE1, ""); //$NON-NLS-1$
    final String variableValue = engine.subst(FILE1);
    assertEquals("123\n{\n456\n", variableValue, VARIABLE_VALUE_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  public void testHashCode()
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
   */
  @Test
  @SuppressFBWarnings("EC_NULL_ARG")
  public void testEquals()
   {
    final TemplateEngine tmpl1 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl2 = new TemplateEngine(HandleUndefined.REMOVE);
    final TemplateEngine tmpl3 = new TemplateEngine(HandleUndefined.KEEP);
    final TemplateEngine tmpl4 = new TemplateEngine(HandleUndefined.REMOVE);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl1), "TemplateEngine11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl2), "TemplateEngine12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl2.equals(tmpl1), "TemplateEngine21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl2.equals(tmpl4), "TemplateEngine24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(tmpl1.equals(tmpl4), "TemplateEngine14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(tmpl3), "TemplateEngine13 are equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl3.equals(tmpl1), "TemplateEngine31 are equal"), //$NON-NLS-1$
      () -> assertFalse(tmpl1.equals(null), "TemplateEngine10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test HandleUndefined.
   */
  @Test
  public void undefined()
   {
    final HandleUndefined undefined = HandleUndefined.COMMENT;
    assertEquals(2, undefined.getAction(), "Result of getAction not as expected"); //$NON-NLS-1$
   }

 }
