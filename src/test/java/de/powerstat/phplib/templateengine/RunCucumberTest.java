/*
 * Copyright (C) 2022-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.phplib.templateengine;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;


/**
 * Run cucumber test.
 */
@SuppressWarnings({"java:S2187", "PMD.TestClassWithoutTestCases"})
@Suite
@IncludeEngines("cucumber")
@SelectPackages("de.powerstat.phplib.templateengine")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "de.powerstat.phplib.templateengine")
public class RunCucumberTest
 {
  /**
   * Default constructor.
   */
  public RunCucumberTest()
   {
    super();
   }

 }
