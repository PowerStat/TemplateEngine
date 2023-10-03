/*
 * Copyright (C) 2022 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;


/**
 * Run cucumber test.
 */
@SuppressWarnings({"java:S2187", "PMD.TestClassWithoutTestCases"})
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("de/powerstat/phplib/templateengine")
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
