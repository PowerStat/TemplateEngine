/*
 * Copyright (C) 2002-2003,2017-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.phplib.templateengine.intern;


import java.util.Objects;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Template block manager.
 */
public final class BlockManager
 {
  /* *
   * Logger.
   */
  // private static final Logger LOGGER = LogManager.getLogger(BlockManager.class);

  /**
   * VManager.
   */
  private static final String VMANAGER = "vManager";

  /**
   * Variable manager reference.
   */
  private final VariableManager variableManager;

  /* *
   * Temporary blocks map.
   */
  // private final Map<String, String> blocks = new ConcurrentHashMap<>();


  /**
   * Copy constructor.
   *
   * @param vManager Variable manager
   * @param bManager Block manager to copy from
   * @throws NullPointerException If vManager or bManager is null
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public BlockManager(final VariableManager vManager, final BlockManager bManager)
   {
    super();
    Objects.requireNonNull(vManager, VMANAGER);
    Objects.requireNonNull(bManager, "bManager"); //$NON-NLS-1$
    variableManager = vManager;
    /*
    for (final Map.Entry<String, String> entry : bManager.blocks.entrySet())
     {
      this.blocks.put(entry.getKey(), entry.getValue());
     }
    */
   }


  /**
   * Constructor.
   *
   * @param vManager Variable manager
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public BlockManager(final VariableManager vManager)
   {
    super();
    Objects.requireNonNull(vManager, VMANAGER);
    variableManager = vManager;
   }


  /**
   * Set template block (cut it from parent template and replace it with a variable).
   *
   * Used for repeatable blocks
   *
   * @param parent Name of parent template variable
   * @param varname Name of template block
   * @param name Name of variable in which the block will be placed - if empty will be the same as varname
   * @return true on sucess otherwise false
   * @throws IllegalStateException When no block with varname is found.
   * @throws NullPointerException If parent or varname is null
   * @throws IllegalArgumentException If parent or varname is empty
   */
  @SuppressWarnings({"PMD.LinguisticNaming", "PMD.AvoidLiteralsInIfCondition"})
  public boolean setBlock(final String parent, final String varname, final String name)
   {
    // asserts
    String internName = name;
    if ((internName == null) || "".equals(internName)) //$NON-NLS-1$
     {
      internName = varname;
     }
    final var pattern = Pattern.compile("<!--\\s+BEGIN " + varname + "\\s+-->(.*)<!--\\s+END " + varname + "\\s+-->", Pattern.DOTALL | Pattern.MULTILINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final var matcher = pattern.matcher(variableManager.getVar(parent));
    final String str = matcher.replaceFirst("{" + internName + "}"); //$NON-NLS-1$ //$NON-NLS-2$
    variableManager.setVar(varname, matcher.group(1));
    variableManager.setVar(parent, str);
    return true;
   }


  /**
   * Returns the string representation of this BlockManager.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "BlockManager[vars=[name, ...]]"
   *
   * @return String representation of this BlockManager.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("BlockManager[vars=").append(variableManager.getVars()).append(']').toString(); //$NON-NLS-1$
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(variableManager);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  @SuppressWarnings("PMD.SimplifyBooleanReturns")
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final BlockManager other))
     {
      return false;
     }
    return variableManager.equals(other.variableManager);
   }

 }
