/*
 * Copyright (C) 2002-2003,2017-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.phplib.templateengine;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.powerstat.phplib.templateengine.intern.BlockManager;
import de.powerstat.phplib.templateengine.intern.FileManager;
import de.powerstat.phplib.templateengine.intern.VariableManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * PHPLib compatible template engine.
 *
 * Unconditionally thread safe.
 * Not serializable, because serialization is dangerous, use Protocol Buffers or JSON instead!
 *
 * @author Kai Hofmann
 * @see <a href="https://sourceforge.net/projects/phplib/">https://sourceforge.net/projects/phplib/</a>
 * @see <a href="https://pear.php.net/package/HTML_Template_PHPLIB">https://pear.php.net/package/HTML_Template_PHPLIB</a>
 */
public final class TemplateEngine
 {
  /* *
   * Logger.
   */
  // private static final Logger LOGGER = LogManager.getLogger(TemplateEngine.class);

  /**
   * Template name constant.
   */
  private static final String TEMPLATE = "template"; //$NON-NLS-1$

  /**
   * Template is empty message.
   */
  private static final String TEMPLATE_IS_EMPTY = "template is empty"; //$NON-NLS-1$

  /**
   * Maximum varname size.
   */
  private static final int MAX_VARNAME_SIZE = 64;

  /**
   * Varname name constant.
   */
  private static final String VARNAME = "varname"; //$NON-NLS-1$

  /**
   * Varname is empty error message constant.
   */
  private static final String VARNAME_IS_EMPTY = "varname is empty"; //$NON-NLS-1$

  /**
   * Varname is to long error message constant.
   */
  private static final String VARNAME_IS_TO_LONG = "varname is to long"; //$NON-NLS-1$

  /**
   * Varname does not match name pattern error message constant.
   */
  private static final String VARNAME_DOES_NOT_MATCH_NAME_PATTERN = "varname does not match name pattern"; //$NON-NLS-1$

  /**
   * Varname regexp pattern.
   */
  private static final Pattern VARNAME_REGEXP = Pattern.compile("^[a-zA-Z0-9_]{1,64}$"); //$NON-NLS-1$

  /**
   * Block matcher regexp.
   */
  private static final Pattern BLOCK_MATCHER_REGEXP = Pattern.compile("\\{([^ \\t\\r\\n}]+)\\}"); //$NON-NLS-1$

  /**
   * Maximum template size.
   */
  private static final int MAX_TEMPLATE_SIZE = 1048576;

  /**
   * Handling of undefined template variables.
   *
   * "remove"/0  =&gt; remove undefined variables
   * "keep"/1    =&gt; keep undefined variables
   * "comment"/2 =&gt; replace undefined variables with comments
   */
  private final HandleUndefined unknowns;

  /**
   * Variable manager.
   */
  private final VariableManager variableManager;

    /**
   * File manager.
     */
  private final FileManager fileManager;

    /**
   * Block manager.
     */
  private final BlockManager blockManager;


  /**
   * Copy constructor.
   *
   * @param engine Template engine
   * @throws NullPointerException If engine is null
   */
  public TemplateEngine(final TemplateEngine engine)
   {
    Objects.requireNonNull(engine, "engine"); //$NON-NLS-1$
    this.unknowns = engine.unknowns;
    this.variableManager = new VariableManager(engine.variableManager);
    this.fileManager = new FileManager(this.variableManager, engine.fileManager);
    this.blockManager = new BlockManager(this.variableManager, engine.blockManager);
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
    this.variableManager = new VariableManager();
    this.fileManager = new FileManager(this.variableManager);
    this.blockManager = new BlockManager(this.variableManager);
   }


  /**
   * Default constructor - unknown variables will be handled as "remove", root is current directory.
   */
  public TemplateEngine()
   {
    this(HandleUndefined.REMOVE);
   }


  /**
   * Copy factory.
   *
   * @param engine TemplateEngine to copy
   * @return A new TemplateEngine instance that is a copy of engine.
   * @throws NullPointerException If engine is null
   */
  public static TemplateEngine newInstance(final TemplateEngine engine)
   {
    return new TemplateEngine(engine);
   }


  /**
   * Get new instance from a UTF-8 encoded text file.
   *
   * @param file Text file (UTF-8 encoded) to load as template
   * @return A new TemplateEngine instance where the template variable name is 'template'
   * @throws FileNotFoundException When the given file does not exist
   * @throws IOException When the given file is to large
   * @throws IllegalArgumentException When the given file is null
   * @throws NullPointerException If file is null
   */
  public static TemplateEngine newInstance(final File file) throws IOException
   {
    Objects.requireNonNull(file, "file"); //$NON-NLS-1$
    if (!file.isFile())
     {
      if (!file.exists())
       {
        throw new FileNotFoundException(file.getAbsolutePath());
       }
      // Load all files (*.tmpl) from directory?
      throw new AssertionError(file.getAbsolutePath() + " is a directory and not a file!"); //$NON-NLS-1$
     }
    final long fileLen = file.length();
    if (fileLen > TemplateEngine.MAX_TEMPLATE_SIZE)
     {
      throw new IOException("file to large: " + fileLen); //$NON-NLS-1$
     }
    final TemplateEngine templ = new TemplateEngine();
    /*
    String filename = file.getName();
    final int extPos = filename.lastIndexOf('.');
    if (extPos > -1)
     {
      filename = filename.substring(0, extPos);
     }
    filename = filename.toLowerCase(Locale.getDefault());
    templ.setFile(filename, file);
    */
    templ.setFile(TemplateEngine.TEMPLATE, file);
    return templ;
   }


  /**
   * Get new instance from a stream.
   *
   * @param stream UTF-8 stream to read the template from
   * @return A new TemplateEngine instance where the template variable name is 'template'.
   * @throws IOException If an I/O error occurs
   * @throws IllegalArgumentException When the given stream is null
   * @throws IllegalStateException If the stream is empty
   * @throws NullPointerException If stream is null
   */
  @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS")
  public static TemplateEngine newInstance(final InputStream stream) throws IOException
   {
    Objects.requireNonNull(stream, "stream"); //$NON-NLS-1$
    final StringBuilder fileBuffer = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))
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
      throw new IllegalStateException("Empty stream"); //$NON-NLS-1$
     }
    final TemplateEngine templ = new TemplateEngine();
    templ.setVar(TemplateEngine.TEMPLATE, fileBuffer.toString());
    return templ;
   }


  /**
   * Get new instance from a string.
   *
   * @param template Template string
   * @return A new TemplateEngine instance where the template variable name is 'template'.
   * @throws IllegalArgumentException When the given string is null or empty
   * @throws NullPointerException If template is null
   */
  public static TemplateEngine newInstance(final String template)
   {
    Objects.requireNonNull(template, TemplateEngine.TEMPLATE);
    if (template.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.TEMPLATE_IS_EMPTY);
     }
    if (template.length() > TemplateEngine.MAX_TEMPLATE_SIZE)
     {
      throw new IllegalArgumentException("template to large"); //$NON-NLS-1$
     }
    // if (!template.matches("^.+$"))
    final TemplateEngine templ = new TemplateEngine();
    templ.setVar(TemplateEngine.TEMPLATE, template);
    return templ;
   }


  /**
   * Set template file for variable.
   *
   * @param newVarname Variable that should hold the template
   * @param newFile Template file UTF-8 encoded
   * @return true when successful (file exists) otherwise false
   * @throws NullPointerException If newVarname or newFile is null
   * @throws IllegalArgumentException If newVarname is empty
   */
  @SuppressWarnings({"PMD.LinguisticNaming", "java:S3457"})
  public boolean setFile(final String newVarname, final File newFile)
   {
    Objects.requireNonNull(newVarname, "newVarname"); //$NON-NLS-1$
    Objects.requireNonNull(newFile, "newFile"); //$NON-NLS-1$
    if (newVarname.isEmpty())
     {
      throw new IllegalArgumentException("newVarname is empty"); //$NON-NLS-1$
     }
    if (newVarname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException("newVarname is to long"); //$NON-NLS-1$
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(newVarname).matches())
     {
      throw new IllegalArgumentException("newVarname does not match name pattern"); //$NON-NLS-1$
     }
    return this.fileManager.addFile(newVarname, newFile);
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
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (varname.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_EMPTY);
     }
    if (varname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_TO_LONG);
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_DOES_NOT_MATCH_NAME_PATTERN);
     }
    return this.variableManager.getVar(varname);
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
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (varname.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_EMPTY);
     }
    if (varname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_TO_LONG);
     }
    if ((value != null) && (value.length() > TemplateEngine.MAX_TEMPLATE_SIZE))
     {
      throw new IllegalArgumentException("value is to large"); //$NON-NLS-1$
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_DOES_NOT_MATCH_NAME_PATTERN);
     }
    this.variableManager.setVar(varname, value);
   }


  /**
   * Set template variable as empty.
   *
   * @param varname Template variable name
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public void setVar(final String varname)
   {
    setVar(varname, "");
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
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (varname.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_EMPTY);
     }
    if (varname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_TO_LONG);
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_DOES_NOT_MATCH_NAME_PATTERN);
     }
    this.variableManager.unsetVar(varname);
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
   * @throws NullPointerException If parent or varname is null
   * @throws IllegalArgumentException If parent or varname is empty
   */
  @SuppressWarnings("PMD.LinguisticNaming")
  public boolean setBlock(final String parent, final String varname, final String name) throws IOException
   {
    Objects.requireNonNull(parent, "parent"); //$NON-NLS-1$
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    if (parent.isEmpty() || varname.isEmpty())
     {
      throw new IllegalArgumentException("parent or varname is empty"); //$NON-NLS-1$
     }
    if ((parent.length() > TemplateEngine.MAX_VARNAME_SIZE) || (varname.length() > TemplateEngine.MAX_VARNAME_SIZE) || (name.length() > TemplateEngine.MAX_VARNAME_SIZE))
     {
      throw new IllegalArgumentException("parent, varname or name is to long"); //$NON-NLS-1$
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(parent).matches() || !TemplateEngine.VARNAME_REGEXP.matcher(varname).matches() || (!name.isEmpty() && (!TemplateEngine.VARNAME_REGEXP.matcher(name).matches())))
     {
      throw new IllegalArgumentException("parent, varname or name does not match name pattern"); //$NON-NLS-1$
     }
    if (!this.fileManager.loadFile(parent))
     {
      return false;
     }
    return this.blockManager.setBlock(parent, varname, name);
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
   * @throws NullPointerException If parent or varname is null
   * @throws IllegalArgumentException If parent or varname is empty
   */
  @SuppressWarnings("PMD.LinguisticNaming")
  public boolean setBlock(final String parent, final String varname) throws IOException
   {
    return setBlock(parent, varname, "");
   }


  /* *
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
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (varname.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_EMPTY);
     }
    if (varname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_TO_LONG);
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_DOES_NOT_MATCH_NAME_PATTERN);
     }
    if (!this.fileManager.loadFile(varname))
     {
      return ""; //$NON-NLS-1$
     }
    return this.variableManager.subst(varname);
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
    Objects.requireNonNull(target, "target"); //$NON-NLS-1$
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (target.isEmpty() || varname.isEmpty())
     {
      throw new IllegalArgumentException("target or varname is empty"); //$NON-NLS-1$
     }
    if ((target.length() > TemplateEngine.MAX_VARNAME_SIZE) || (varname.length() > TemplateEngine.MAX_VARNAME_SIZE))
     {
      throw new IllegalArgumentException("target or varname is to long"); //$NON-NLS-1$
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(target).matches() || !TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException("target or varname does not match name pattern"); //$NON-NLS-1$
     }
    if (!this.fileManager.loadFile(varname))
     {
      return ""; //$NON-NLS-1$
     }
    return this.variableManager.parse(target, varname, append);
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
   * @throws NullPointerException If target or varname is null
   * @throws IllegalArgumentException If target or varname is empty
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
  public List<String> getVars()
   {
    return this.variableManager.getVars();
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
    Objects.requireNonNull(varname, TemplateEngine.VARNAME);
    if (varname.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_EMPTY);
     }
    if (varname.length() > TemplateEngine.MAX_VARNAME_SIZE)
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_IS_TO_LONG);
     }
    if (!TemplateEngine.VARNAME_REGEXP.matcher(varname).matches())
     {
      throw new IllegalArgumentException(TemplateEngine.VARNAME_DOES_NOT_MATCH_NAME_PATTERN);
     }
    if (!this.fileManager.loadFile(varname))
     {
      return Collections.emptyList();
     }
    return this.variableManager.getUndefined(varname);
   }


  /**
   * Handle undefined template variables after parsing has happened.
   *
   * @param template Template to parse for unknown variables
   * @return Modified template as specified by the "unknowns" setting
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public String finish(final String template)
   {
    Objects.requireNonNull(template, TemplateEngine.TEMPLATE);
    if (template.isEmpty())
     {
      throw new IllegalArgumentException(TemplateEngine.TEMPLATE_IS_EMPTY);
     }
    if (template.length() > TemplateEngine.MAX_TEMPLATE_SIZE)
     {
      throw new IllegalArgumentException("template is to large"); //$NON-NLS-1$
     }
    // if (!template.matches("^.+$"))
    String result = template;
    final Matcher matcher = TemplateEngine.BLOCK_MATCHER_REGEXP.matcher(result);
    switch (this.unknowns)
     {
      case KEEP:
        break;
      case REMOVE:
        result = matcher.replaceAll(""); //$NON-NLS-1$
        break;
      case COMMENT:
        result = matcher.replaceAll("<!-- Template variable '$1' undefined -->"); //$NON-NLS-1$
        break;
      default: // For the case that enum HandleUndefined will be extended!
        throw new AssertionError(this.unknowns);
     }
    return result;
   }


  /**
   * Shortcut for finish(getVar(varname)).
   *
   * @param varname Name of template variable
   * @return Value of template variable
   * @throws NullPointerException If varname is null
   * @throws IllegalArgumentException If varname is empty
   */
  public String get(final String varname)
   {
    return finish(getVar(varname));
   }


  /**
   * Returns the string representation of this TemplatEngine.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "TemplateEngine[unknowns=REMOVE, vManager=[name, ...]]"
   *
   * @return String representation of this TemplatEngine.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("TemplateEngine[unknowns=").append(this.unknowns).append(", vManager=").append(this.variableManager).append(", fManager=").append(this.fileManager).append(", bManager=").append(this.blockManager).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
    return Objects.hash(this.unknowns, this.variableManager, this.fileManager, this.blockManager);
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
    if (!(obj instanceof TemplateEngine))
     {
      return false;
     }
    final TemplateEngine other = (TemplateEngine)obj;
    return (this.unknowns == other.unknowns) && this.variableManager.equals(other.variableManager) && this.fileManager.equals(other.fileManager) && this.blockManager.equals(other.blockManager);
   }

 }
