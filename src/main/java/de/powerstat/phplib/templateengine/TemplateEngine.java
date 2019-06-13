/*
 * Copyright (C) 2002-2003,2017-2019 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * PHPLib compatible template engine.
 *
 * @author Kai Hofmann
 * @see <a href="https://sourceforge.net/projects/phplib/">https://sourceforge.net/projects/phplib/</a>
 * @see <a href="https://pear.php.net/package/HTML_Template_PHPLIB">https://pear.php.net/package/HTML_Template_PHPLIB</a>
 */
public final class TemplateEngine
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(TemplateEngine.class);

  /**
   * File name map.
   */
  private final transient Map<String, File> files = new ConcurrentHashMap<>();

  /**
   * Temporary variables map.
   */
  private final transient Map<String, String> tempVars = new ConcurrentHashMap<>();

  /**
   * Handling of undefined template variables.
   *
   * "remove"  =&gt; remove undefined variables
   * "comment" =&gt; replace undefined variables with comments
   * "keep"    =&gt; keep undefined variables
   */
  private transient HandleUndefined unknowns = HandleUndefined.remove;

  /**
   * Enum for handling of undefined variables.
   */
  public enum HandleUndefined
   {
    /**
     * Keep variables.
     */
    keep,

    /**
     * Change to XML comments.
     */
    comment,

    /**
     * Remove variables.
     */
    remove
   }


  /**
   * Constructor.
   *
   * @param unknowns Handling of unknown template variables
   * @see HandleUndefined
   */
  public TemplateEngine(final HandleUndefined unknowns)
   {
    this.unknowns = unknowns;
   }


  /**
   * Default constructor - unknown variables will be handled as "remove", root is current directory.
   */
  public TemplateEngine()
   {
    this(HandleUndefined.remove);
   }


  /**
   * Handling of unknown template variables during parsing.
   *
   * @param newUnknowns How to handle unknown variables.
   * @see HandleUndefined
   */
  public void setUnknowns(final HandleUndefined newUnknowns)
   {
    this.unknowns = newUnknowns;
   }


  /**
   * Set template file for variable.
   *
   * @param newVarname Variable that should hold the template
   * @param newFile Template file
   * @return true when successful (file exists) otherwise false
   */
  public boolean setFile(final String newVarname, final File newFile)
   {
    boolean exists = newFile.exists();
    if (exists)
     {
      this.files.put(newVarname, newFile);
     }
    else
     {
      try (InputStream stream = this.getClass().getResourceAsStream("/" + newFile.getName())) //$NON-NLS-1$
       {
        if (stream != null)
         {
          exists = true;
          this.files.put(newVarname, newFile);
         }
       }
      catch (final IOException e)
        {
         // ignore
        }
     }
    return exists;
   }


  /**
   * Load template file if required.
   *
   * @param varname Variable to read from file
   * @return true if successful otherwise false
   * @throws FileNotFoundException File not found
   * @throws IOException IO exception
   */
  private boolean loadfile(final String varname) throws IOException
   {
    if (this.tempVars.containsKey(varname)) // Already loaded?
     {
      return true;
     }
    final StringBuilder fileBuffer = new StringBuilder();
    final File file = this.files.get(varname);
    if (file == null)
     {
      return false;
     }
    InputStream istream = this.getClass().getResourceAsStream("/" + file.getName()); //$NON-NLS-1$ // Read from classpath/jar
    if (istream == null)
     {
      istream = Files.newInputStream(this.files.get(varname).toPath(), StandardOpenOption.READ); // Read from filesystem
     }
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(istream, StandardCharsets.UTF_8)))
     {
      String line = reader.readLine();
      while (line != null)
       {
        fileBuffer.append(line);
        fileBuffer.append('\n');
        line = reader.readLine();
       }
     }
    if (fileBuffer.length() == 0)
     {
      return false;
     }
    setVar(varname, fileBuffer.toString());
    return true;
   }


  /**
   * Get template variable value.
   *
   * @param varname Template variable name
   * @return Template variables value
   */
  public String getVar(final String varname)
   {
    return this.tempVars.containsKey(varname) ? this.tempVars.get(varname) : ""; //$NON-NLS-1$
   }


  /**
   * Set template variables value.
   *
   * @param varname Template variable name
   * @param value Template variable value
   */
  public void setVar(final String varname, final String value)
   {
    this.tempVars.put(varname, (value == null) ? "" : value); //$NON-NLS-1$
   }


  /**
   * Set template variable as empty.
   *
   * @param varname Template variable name
   */
  public void setVar(final String varname)
   {
    setVar(varname, ""); //$NON-NLS-1$
   }


  /**
   * Unset template variable.
   *
   * @param varname Template variable name
   */
  public void unsetVar(final String varname)
   {
    /* String value = */ this.tempVars.remove(varname);
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
   * @throws IOException IO exception
   * @throws IllegalStateException When no block with varname is found.
   */
  public boolean setBlock(final String parent, final String varname, final String name) throws IOException, IllegalStateException
   {
    if (!loadfile(parent))
     {
      return false;
     }
    String internName = name;
    if ("".equals(internName)) //$NON-NLS-1$
     {
      internName = varname;
     }
    final Pattern pattern = Pattern.compile("<!--\\s+BEGIN " + varname + "\\s+-->(.*)<!--\\s+END " + varname + "\\s+-->", Pattern.DOTALL | Pattern.MULTILINE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final Matcher matcher = pattern.matcher(getVar(parent));
    final String str = matcher.replaceFirst("{" + internName + "}"); //$NON-NLS-1$ //$NON-NLS-2$
    setVar(varname, matcher.group(1));
    setVar(parent, str);
    return true;
   }


  /**
   * Set template block (cut it from parent template and replace it with a variable).
   *
   * Used for on/off blocks
   *
   * @param parent Name of parent template variable
   * @param varname Name of template block
   * @return true on sucess otherwise false
   * @throws IOException IO exception
   * @throws IllegalStateException When no block with varname is found.
   */
  public boolean setBlock(final String parent, final String varname) throws IOException, IllegalStateException
   {
    return setBlock(parent, varname, ""); //$NON-NLS-1$
   }


  /**
   * Replace variables old version.
   *
   * @param varname Template/Block name
   * @return Template/Block with replaced variables
   */
  /*
  private String replaceVarsOld(String varname)
   {
    // loop over all known variables an replace them
    if (!this.tempVars.isEmpty())
     {
      final Set<Entry<String, String>> tempVarsSet = this.tempVars.entrySet();
      final Iterator<Entry<String, String>> iter = tempVarsSet.iterator();
      while (iter.hasNext())
       {
        final Map.Entry<String, String> mapEntry = iter.next();
        // convert into regexp (special char filter)
        final Pattern pattern = Pattern.compile("\\{" + mapEntry.getKey() + "\\}"); //$NON-NLS-1$ //$NON-NLS-2$
        final Matcher matcher = pattern.matcher(varname);
        varname = matcher.replaceAll(mapEntry.getValue());
       }
     }
    return varname;
   }
  */


  /**
   * Replace variables new version.
   *
   * @param varname Template/Block name
   * @return Template/Block with replaced variables
   */
  private String replaceVarsNew(String varname)
   {
    // Get variable names to replace from varname
    final Pattern patternTemplate = Pattern.compile("\\{([^}\n\r\t :]+)\\}"); //$NON-NLS-1$
    final Matcher matcherTemplate = patternTemplate.matcher(varname);
    final Set<String> varsSetTemplate = new TreeSet<>();
    while (matcherTemplate.find())
     {
      final String varnameTemplate = varname.substring(matcherTemplate.start() + 1, matcherTemplate.end() - 1);
      if (this.tempVars.containsKey(varnameTemplate))
       {
        varsSetTemplate.add(varnameTemplate);
       }
     }
    for (final String varName : varsSetTemplate)
     {
      varname = varname.replaceAll("\\{" + varName + "\\}", getVar(varName));
     }
    return varname;
       }


  /**
   * Substitute variable with its content.
   *
   * @param varname Variable name
   * @return Replaced variable content or null
   * @throws IOException File not found or IO exception
   */
  public String subst(final String varname) throws IOException
   {
    if (!loadfile(varname))
     {
      return null;
     }
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
   */
  public String parse(final String target, final String varname, final boolean append) throws IOException
   {
    final String str = subst(varname);
    setVar(target, (append ? getVar(target) : "") + str); //$NON-NLS-1$
    return str;
   }


  /**
   * Parse a variable and replace all variables within it by their content.
   *
   * Don't append
   *
   * @param target Target for parsing operation
   * @param varname Parse the content of this variable
   * @return Variables content after parsing
   * @throws IOException File not found or IO exception
   */
  public String parse(final String target, final String varname) throws IOException
   {
    return parse(target, varname, false);
   }


  /**
   * Get list of all template variables.
   *
   * @return Array with names of template variables
   */
  public String[] getVars()
   {
    if (this.tempVars.isEmpty())
     {
      return new String[0];
     }
    final String[] result = new String[this.tempVars.size()];
    final Set<Entry<String, String>> tempVarsSet = this.tempVars.entrySet();
    final Iterator<Entry<String, String>> iter = tempVarsSet.iterator();
    int ctr = 0;
    while (iter.hasNext())
     {
      final Map.Entry<String, String> mapEntry = iter.next();
      result[ctr++] = mapEntry.getKey(); // + " = " + mapEntry.getValue();
     }
    return result;
   }


  /**
   * Get list with all undefined template variables.
   *
   * @param varname Variable to parse for undefined variables
   * @return List with undefined template variables names
   * @throws IOException  File not found or IO exception
   */
  public List<String> getUndefined(final String varname) throws IOException
   {
    if (!loadfile(varname))
     {
      return new ArrayList<>();
     }
    final Pattern pattern = Pattern.compile("\\{([^ \\t\\r\\n}]+)\\}"); //$NON-NLS-1$
    final Matcher matcher = pattern.matcher(getVar(varname));
    boolean result = matcher.find();
    final List<String> undefvars = new ArrayList<>();
    while (result)
     {
      final String vname = matcher.group(1);
      if (!this.tempVars.containsKey(vname) && !undefvars.contains(vname))
       {
        undefvars.add(vname);
       }
      result = matcher.find();
     }
    return undefvars;
   }


  /**
   * Handle undefined template variables after parsing has happened.
   *
   * @param str Template variable to parse for unknown variables
   * @return Modified str as specified by the "unknowns" setting
   */
  public String finish(final String str)
   {
    String result = str;
    final Pattern pattern = Pattern.compile("\\{([^ \\t\\r\\n}]+)\\}"); //$NON-NLS-1$
    final Matcher matcher = pattern.matcher(result);
    switch (this.unknowns)
     {
      case keep:
        break;
      case remove:
        result = matcher.replaceAll(""); //$NON-NLS-1$
        break;
      case comment:
        result = matcher.replaceAll("<!-- Template variable '$1' undefined -->"); //$NON-NLS-1$
        break;
      /*
      default: // Only for the case that enum HandleUndefined will be extended.
        if (LOGGER.isDebugEnabled())
         {
          LOGGER.debug("Unsupported unknowns: " + this.unknowns); //$NON-NLS-1$
         }
      */
     }
    return result;
   }


  /**
   * Shortcut for finish(getVar(varname)).
   *
   * @param varname Name of template variable
   * @return Value of template variable
   */
  public String get(final String varname)
   {
    return finish(getVar(varname));
   }

 }
