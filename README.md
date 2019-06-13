# PowerStat's TemplateEngine

This TemplateEngine is a Java port with optimizations of the original PHPLib TemplateEngine

See:

* [PHPLib](https://sourceforge.net/projects/phplib/)
* [PEAR - PHP Extension and Application Repository](https://pear.php.net/package/HTML_Template_PHPLIB)

## Installation

Because this library is only useful for developers the installation depends on your build environment.

For example when using Apache Maven you could add the following dependency to your project:

    <dependency>
      <groupId>de.powerstat.phplib</groupId>
      <artifactId>templateengine</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

Other build tools like gradle will work analogous.

To compile this project yourself you could use:

    mvn clean install org.pitest:pitest-maven:mutationCoverage site
    
or simply:

     mvn clean install

## Usage

For usage in your own projects please read the Javadoc's and follow the examples in the unittests.

## Contributing

If you would like to contribute to this project please read [How to contribute](CONTRIBUTING.md).

## License

This code is licensed under the [Apache License Version 2.0](LICENSE.md).
