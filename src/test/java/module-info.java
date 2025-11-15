/*
 * Copyright (C) 2019-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */


/**
 * Module for PowerStat's PHPLib like TemplateEngine.
 */
open module de.powerstat.phplib.templateengine
 {
  exports de.powerstat.phplib.templateengine;

  // requires java.io;
  // requires java.nio;
  // requires java.util;

  requires org.apache.logging.log4j;
  requires com.github.spotbugs.annotations;

  requires org.junit.jupiter.api;
  requires org.junit.platform.launcher;
  requires org.junit.platform.suite.api;
  requires io.cucumber.java;
  requires io.cucumber.junit.platform.engine;
  requires nl.jqno.equalsverifier;

 }
