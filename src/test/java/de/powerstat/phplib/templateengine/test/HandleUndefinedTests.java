/*
 * Copyright (C) 2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.phplib.templateengine.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.powerstat.phplib.templateengine.HandleUndefined;


/**
 * HandleUndefined tests.
 */
final class HandleUndefinedTests
 {
  /**
   * Default constructor.
   */
  /* default */ HandleUndefinedTests()
   {
    super();
   }


  /**
   * Test getAction.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, HandleUndefined.REMOVE.getAction(), "REMOVE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, HandleUndefined.KEEP.getAction(), "KEEP action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, HandleUndefined.COMMENT.getAction(), "COMMENT action not as expected") //$NON-NLS-1$
    );
   }

 }
