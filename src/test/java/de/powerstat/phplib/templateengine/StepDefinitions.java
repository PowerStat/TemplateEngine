/*
 * Copyright (C) 2022 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


/**
 * Cucumber step definitions.
 */
@SuppressWarnings({"PMD.MethodNamingConventions", "checkstyle:MethodName"})
public class StepDefinitions
 {
  /**
   * Template file 1 path constant.
   */
  private static final String TEMPLATE1_TMPL = "target/test-classes/templates/template1.tmpl"; //$NON-NLS-1$

  /**
   * Template file 2 path constant.
   */
  private static final String TEMPLATE2_TMPL = "target/test-classes/templates/template2.tmpl"; //$NON-NLS-1$

  /**
   * Template block 1 block name constant.
   */
  private static final String BLK1_BLK = "BLK1_BLK"; //$NON-NLS-1$

  /**
   * Template block 1 name constant.
   */
  private static final String BLK1 = "BLK1"; //$NON-NLS-1$

  /**
   * Template name constant.
   */
  private static final String TEMPLATE = "template"; //$NON-NLS-1$

  /**
   * Template variable 1 name constant.
   */
  private static final String VARIABLE1 = "variable1"; //$NON-NLS-1$

  /**
   * Hello world constant.
   */
  private static final String HELLO_WORLD = "hello world"; //$NON-NLS-1$

  /**
   * Result not as expected constant.
   */
  private static final String RESULT_NOT_AS_EXPECTED = "Result not as expected"; //$NON-NLS-1$



  /**
   * Template engine.
   */
  private TemplateEngine engine;


  /**
   * Default constructor.
   */
  public StepDefinitions()
   {
    super();
   }


  /**
   * Given.
   *
   * @throws IOException IO exception
   */
  @Given("A template with a variable")
  public void a_template_with_a_variable() throws IOException
   {
    this.engine = TemplateEngine.newInstance(new File(StepDefinitions.TEMPLATE1_TMPL));
   }


  /**
   * When.
   */
  @When("The template is processed")
  public void the_template_is_processed()
   {
    // Nothing to do.
   }


  /**
   * Then.
   *
   * @throws IOException IO exception
   */
  @Then("The variable is replaced with the value hello world")
  public void the_variable_is_replaced_with_the_value_hello_world() throws IOException
   {
    this.engine.setVar(StepDefinitions.VARIABLE1, StepDefinitions.HELLO_WORLD);
    final String result = this.engine.subst(StepDefinitions.TEMPLATE);
    assertEquals("123\nhello world\n456\n", result, StepDefinitions.RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Given.
   *
   * @throws IOException IO exception
   */
  @Given("A template with a block")
  public void a_template_with_a_block() throws IOException
   {
    this.engine = TemplateEngine.newInstance(new File(StepDefinitions.TEMPLATE2_TMPL));
   }


  /**
   * Then.
   *
   * @throws IOException IO exception
   */
  @Then("The block is replaced with the value hello world")
  public void the_block_is_replaced_with_the_value_hello_world() throws IOException
   {
    /* final boolean successBlock = */ this.engine.setBlock(StepDefinitions.TEMPLATE, StepDefinitions.BLK1, StepDefinitions.BLK1_BLK);
    this.engine.setVar(StepDefinitions.BLK1, StepDefinitions.HELLO_WORLD);
    this.engine.setVar(StepDefinitions.VARIABLE1, ""); //$NON-NLS-1$
    this.engine.setVar("variable3", ""); //$NON-NLS-1$ //$NON-NLS-2$
    /* String parseResult = */ this.engine.parse(StepDefinitions.BLK1_BLK, StepDefinitions.BLK1, false);
    final String result = this.engine.subst(StepDefinitions.TEMPLATE);
    assertEquals("123\n\n456\nhello world\ndef\n\nghi\n", result, StepDefinitions.RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }

 }
