/*
 * Copyright (C) 2002-2003,2017-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine;


/**
 * Enum for handling of undefined variables.
 */
public enum HandleUndefined
 {
  /**
   * Remove variables.
   */
  REMOVE(0),

  /**
   * Keep variables.
   */
  KEEP(1),

  /**
   * Change to XML comments.
   */
  COMMENT(2);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  HandleUndefined(final int action)
   {
    this.action = action;
   }


  /**
   * Get action number.
   *
   * @return Action number
   */
  public int getAction()
   {
    return this.action;
   }

 }
