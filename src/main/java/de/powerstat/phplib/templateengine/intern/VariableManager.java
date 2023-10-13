/*
 * Copyright (C) 2002-2003,2017-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine.intern;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Template variable manager.
 */
public class VariableManager
 {
  /* *
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(VariableManager.class);

  /**
   * Template matcher regexp pattern.
   */
  private static final Pattern TEMPLATE_MATCHER_REGEXP = Pattern.compile("\\{([^{^}\n\r\t :]+)\\}"); //$NON-NLS-1$

  /**
   * Block matcher regexp.
   */
  private static final Pattern BLOCK_MATCHER_REGEXP = Pattern.compile("\\{([^ \\t\\r\\n}]+)\\}"); //$NON-NLS-1$

  /**
   * Temporary variables map.
   */
  private final Map<String, String> vars = new ConcurrentHashMap<>();


  /**
   * Copy constructor.
   *
   * @param vManager Variable manager to copy from
   * @throws NullPointerException If vManager is null
   */
  public VariableManager(final VariableManager vManager)
   {
    super();
    Objects.requireNonNull(vManager, "vManager"); //$NON-NLS-1$
    for (final Map.Entry<String, String> entry : vManager.vars.entrySet())
     {
      this.vars.put(entry.getKey(), entry.getValue());
     }
   }


  /**
   * Default constructor.
   */
  public VariableManager()
   {
    super();
   }


  /**
   * Exists variable.
   *
   * @param varname Variable name
   * @return true: Variable exists; false otherwise
   */
  public boolean existsVar(final String varname)
   {
    return this.vars.containsKey(varname);
   }


  /**
   * Get template variable value.
   *
   * @param varname Template variable name
   * @return Template variables value
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public String getVar(final String varname)
   {
    final String value = this.vars.get(varname);
    return (value == null) ? "" : value; //$NON-NLS-1$
   }


  /**
   * Set template variables value.
   *
   * @param varname Template variable name
   * @param value Template variable value, could  be null
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public void setVar(final String varname, final String value)
   {
    // if (!value.matches("^.+$"))
    this.vars.put(varname, (value == null) ? "" : value); //$NON-NLS-1$
   }


  /**
   * Unset template variable.
   *
   * @param varname Template variable name
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public void unsetVar(final String varname)
   {
    /* String value = */ this.vars.remove(varname);
   }


  /**
   * Get list with all undefined template variables.
   *
   * @param varname Variable to parse for undefined variables
   * @return List with undefined template variables names
   * @throws IOException  File not found or IO exception
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public List<String> getUndefined(final String varname) throws IOException
   {
    // TODO asserts
    final Matcher matcher = VariableManager.BLOCK_MATCHER_REGEXP.matcher(getVar(varname));
    boolean result = matcher.find();
    final List<String> undefvars = new ArrayList<>();
    while (result)
     {
      final String vname = matcher.group(1);
      if (!this.vars.containsKey(vname) && !undefvars.contains(vname))
       {
        undefvars.add(vname);
       }
      result = matcher.find();
     }
    return Collections.unmodifiableList(undefvars);
   }


  /* *
   * Replace variables old version.
   *
   * @param block Template block
   * @return Template/Block with replaced variables
   */
  /*
  private String replaceVarsOld(String block)
   {
    assert (block != null) && !block.isEmpty();
    // loop over all known variables an replace them
    if (!this.vars.isEmpty())
     {
      final Set<Entry<String, String>> tempVarsSet = this.vars.entrySet();
      for (Entry<String, String> mapEntry : tempVarsSet)
       {
        // convert into regexp (special char filter)
        block = Pattern.compile("\\{" + mapEntry.getKey() + "\\}").matcher(block).replaceAll(mapEntry.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
       }
     }
    return block;
   }
  */


  /**
   * Replace variables new version.
   *
   * @param block Template block
   * @return Template/Block with replaced variables
   */
  private String replaceVarsNew(final String block)
   {
    // assert (block != null) && !block.isEmpty();
    // assert block.matches("^.+$")
    // Get variable names to replace from varname
    final Matcher matcherTemplate = VariableManager.TEMPLATE_MATCHER_REGEXP.matcher(block);
    final Set<String> varsSetTemplate = new TreeSet<>();
    while (matcherTemplate.find())
     {
      final String varnameTemplate = block.substring(matcherTemplate.start() + 1, matcherTemplate.end() - 1);
      if (this.vars.containsKey(varnameTemplate))
       {
        varsSetTemplate.add(varnameTemplate);
       }
     }
    String resBlock = block;
    for (final String varName : varsSetTemplate)
     {
      resBlock = resBlock.replaceAll("\\{" + varName + "\\}", getVar(varName)); //$NON-NLS-1$ //$NON-NLS-2$
     }
    return resBlock;
   }


  /**
   * Substitute variable with its content.
   *
   * @param varname Variable name
   * @return Replaced variable content or empty string
   * @throws IOException File not found or IO exception
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public String subst(final String varname) throws IOException
   {
    // return replaceVarsOld(getVar(varname));
    return replaceVarsNew(getVar(varname));
   }


  /**
   * Parse a variable and replace all variables within it by their content.
   *
   * @param target Target for parsing operation
   * @param varname Parse the content of this variable
   * @param append true for appending blocks to target, otherwise false for replacing targets content
   * @return Variables content after parsing
   * @throws IOException File not found or IO exception
   * @throws NullPointerException If target or varname is null
   * @throws IllegalArgumentException If target or varname is empty
   */
  @SuppressWarnings("java:S2301")
  public String parse(final String target, final String varname, final boolean append) throws IOException
   {
    if (LOGGER.isDebugEnabled())
     {
      LOGGER.debug("varname: " + varname);
     }
    final String str = subst(varname);
    if (LOGGER.isDebugEnabled())
     {
      LOGGER.debug("str: " + str);
     }
    setVar(target, (append ? getVar(target) : "") + str); //$NON-NLS-1$
    return str;
   }


  /**
   * Get list of all template variables.
   *
   * @return Array with names of template variables
   */
  public List<String> getVars()
   {
    if (this.vars.isEmpty())
     {
      return Collections.emptyList();
     }
    final List<String> result = new ArrayList<>(this.vars.size());
    for (final Entry<String, String> entry : this.vars.entrySet())
     {
      result.add(entry.getKey()); // entry.getValue();
     }
    return Collections.unmodifiableList(result);
   }


  /**
   * Returns the string representation of this  VariableManager.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * " VariableManager[vars=[name, ...]]"
   *
   * @return String representation of this  VariableManager.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("VariableManager[vars=").append(getVars()).append(']').toString(); //$NON-NLS-1$
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
    return Objects.hash(this.vars);
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
    if (!(obj instanceof VariableManager))
     {
      return false;
     }
    final VariableManager other = (VariableManager)obj;
    return this.vars.equals(other.vars);
   }

 }
