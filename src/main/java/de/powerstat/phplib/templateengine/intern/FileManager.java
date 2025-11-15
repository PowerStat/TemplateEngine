/*
 * Copyright (C) 2002-2003,2017-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.phplib.templateengine.intern;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Template file manager.
 */
public final class FileManager
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(FileManager.class);

  /**
   * Maximum template size.
   */
  private static final int MAX_TEMPLATE_SIZE = 1048576;

  /**
   * File path separator.
   */
  private static final String FILEPATH_SEPARATOR = "/"; //$NON-NLS-1$

  /**
   * Variable manager reference.
   */
  private final VariableManager variableManager;

  /**
   * File name map.
   */
  private final Map<String, File> files = new ConcurrentHashMap<>();


  /**
   * Copy constructor.
   *
   * @param vManager Variable manager
   * @param fManager File manager to copy from
   * @throws NullPointerException If vManager or fManager is null
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public FileManager(final VariableManager vManager, final FileManager fManager)
   {
    super();
    Objects.requireNonNull(vManager, "vManager"); //$NON-NLS-1$
    Objects.requireNonNull(fManager, "fManager"); //$NON-NLS-1$
    variableManager = vManager;
    for (final Map.Entry<String, File> entry : fManager.files.entrySet())
     {
      files.put(entry.getKey(), entry.getValue());
     }
   }


  /**
   * Constructor.
   *
   * @param vManager Variable manager
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public FileManager(final VariableManager vManager)
   {
    super();
    Objects.requireNonNull(vManager, "vManager"); //$NON-NLS-1$
    variableManager = vManager;
   }


  /**
   * Add template file for variable.
   *
   * @param newVarname Variable that should hold the template
   * @param newFile Template file UTF-8 encoded
   * @return true when successful (file exists) otherwise false
   * @throws NullPointerException If newVarname or newFile is null
   * @throws IllegalArgumentException If newVarname is empty
   */
  @SuppressWarnings({"PMD.LinguisticNaming", "java:S3457"})
  public boolean addFile(final String newVarname, final File newFile)
   {
    // asserts
    boolean exists = newFile.exists();
    if (exists)
     {
      if (newFile.length() > MAX_TEMPLATE_SIZE)
       {
        throw new IllegalArgumentException("newFile to large"); //$NON-NLS-1$
       }
      files.put(newVarname, newFile);
     }
    else
     {
      try (InputStream stream = this.getClass().getResourceAsStream(FILEPATH_SEPARATOR + newFile.getName()))
       {
        if (stream != null)
         {
          exists = true;
          files.put(newVarname, newFile);
         }
       }
      catch (final IOException ignored)
       {
        // exists is already false
        LOGGER.warn("File does not exist: " + newFile.getAbsolutePath(), ignored); //$NON-NLS-1$
       }
     }
    return exists;
   }


  /**
   * Exists file for varname.
   *
   * @param varname Variable to read from file
   * @return true if file exists, false otherwise
   */
  public boolean existsFile(final String varname)
   {
    final var file = files.get(varname);
    return (file != null);
   }


  /**
   * Load template file (UTF-8 encoded) if required.
   *
   * @param varname Variable to read from file
   * @return true if successful otherwise false
   * @throws FileNotFoundException File not found
   * @throws IOException IO exception
   */
  @SuppressWarnings("PMD.CloseResource")
  public boolean loadFile(final String varname) throws IOException
   {
    // assert (varname != null) && !varname.isEmpty() && (varname.length() <= TemplateEngine.MAX_VARNAME_SIZE);
    if (variableManager.existsVar(varname)) // Already loaded?
     {
      return true;
     }
    final var file = files.get(varname);
    if (file == null)
     {
      return false;
     }
    InputStream istream = this.getClass().getResourceAsStream(FILEPATH_SEPARATOR + file.getName()); // Read from classpath/jar
    if (istream == null)
     {
      istream = Files.newInputStream(files.get(varname).toPath(), StandardOpenOption.READ); // Read from filesystem
     }
    final var fileBuffer = new StringBuilder();
    try (var reader = new BufferedReader(new InputStreamReader(istream, StandardCharsets.UTF_8)))
     {
      String line = reader.readLine();
      while (line != null)
       {
        fileBuffer.append(line).append('\n');
        line = reader.readLine();
       }
     }
    if (fileBuffer.length() == 0)
     {
      return false;
     }
    variableManager.setVar(varname, fileBuffer.toString());
    return true;
   }


  /**
   * Returns the string representation of this FileManager.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "FileManager[files=[name, ...]]"
   *
   * @return String representation of this FileManager.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("FileManager[").append("files=").append(files.values().stream().map(File::getName).reduce((s1, s2) -> s1 + ", " + s2)).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
    return Objects.hash(files, variableManager);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final FileManager other))
     {
      return false;
     }
    return files.equals(other.files) && variableManager.equals(other.variableManager);
   }

 }
