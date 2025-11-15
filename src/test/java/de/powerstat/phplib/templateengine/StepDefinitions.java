/*
 * Copyright (C) 2022-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
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
   * Result from variable template.
   */
  private String resultVar = ""; //$NON-NLS-1$

  /**
   * Result from block template.
   */
  private String resultBlock = ""; //$NON-NLS-1$


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
    this.engine = TemplateEngine.newInstance(new File(TEMPLATE1_TMPL));
   }


  /**
   * When.
   *
   * @throws IOException IO exception
   */
  @When("The template is processed to replace the variable")
  public void the_template_is_processed_to_replace_the_variable() throws IOException
   {
    this.engine.setVar(VARIABLE1, HELLO_WORLD);
    this.resultVar = this.engine.subst(TEMPLATE);
   }


  /**
   * Then.
   */
  @Then("The variable is replaced with the value hello world")
  public void the_variable_is_replaced_with_the_value_hello_world()
   {
    assertEquals("123\nhello world\n456\n", this.resultVar, RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Given.
   *
   * @throws IOException IO exception
   */
  @Given("A template with a block")
  public void a_template_with_a_block() throws IOException
   {
    this.engine = TemplateEngine.newInstance(new File(TEMPLATE2_TMPL));
   }


  /**
   * When.
   *
   * @throws IOException IO exception
   */
  @When("The template is processed to replace the block")
  public void the_template_is_processed_to_replace_the_block() throws IOException
   {
    /* final boolean successBlock = */ this.engine.setBlock(TEMPLATE, BLK1, BLK1_BLK);
    this.engine.setVar(BLK1, HELLO_WORLD);
    this.engine.setVar(VARIABLE1, ""); //$NON-NLS-1$
    this.engine.setVar("variable3", ""); //$NON-NLS-1$ //$NON-NLS-2$
    /* String parseResult = */ this.engine.parse(BLK1_BLK, BLK1, false);
    this.resultBlock = this.engine.subst(TEMPLATE);
   }


  /**
   * Then.
   */
  @Then("The block is replaced with the value hello world")
  public void the_block_is_replaced_with_the_value_hello_world()
   {
    assertEquals("123\n\n456\nhello world\ndef\n\nghi\n", this.resultBlock, RESULT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }

 }
