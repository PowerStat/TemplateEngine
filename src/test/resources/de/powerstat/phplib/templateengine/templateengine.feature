#Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
Feature: TemplateEngine
  PHPLIB Template Engine

  Scenario: A template with a variable to replace with content
    Given A template with a variable
    When The template is processed to replace the variable
    Then The variable is replaced with the value hello world

  Scenario: A template with a block to replace with content
    Given A template with a block
    When The template is processed to replace the block
    Then The block is replaced with the value hello world
