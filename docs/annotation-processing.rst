=====================
Annotation processing
=====================

.. contents::
   :depth: 3

Doma uses `Pluggable Annotation Processing API <https://www.jcp.org/en/jsr/detail?id=269>`_ at compile time.

In this document, we describe the options for the annotation processors in Doma
and show you how to pass them to build tools.

Options
=======

doma.dao.package
  The package that the generated implementation classes of interfaces annotated with ``@Dao`` belong to.
  The specified value overrides the value of doma.dao.subpackage.
  The default value is the same package as the one the interfaces annotated with ``@Dao`` belong to.

doma.dao.subpackage
  The subpackage that the generated implementation classes of interfaces annotated with ``@Dao`` belong to.
  The specified value is overridden by the value of doma.dao.package.
  If this value is ``impl`` and the package of interfaces annotated with ``@Dao`` is ``example.dao``,
  the generated implementation classes belong to the package ``example.dao.impl``.

doma.dao.suffix
  The name suffix that the generated implementation classes of interfaces annotated with ``@Dao`` have.
  If this value is ``Bean`` and the simple name of the interface annotated with ``@Dao`` is ``EmployeeDao``,
  the simple name of the generated implementation class is ``EmployeeDaoBean``.
  The default value is ``Impl``.

doma.debug
  Whether to output the debug log in annotation processing.
  If the value is ``true``, the annotation processors output the debug log.
  The default value is ``false``.

doma.domain.converters
  The full qualified names of the classes annotated with ``@DomainConverters``.
  The names are described as comma separated list.
  This value are used to find external domain classes.

doma.entity.field.prefix
  The name prefix that the fields of the generated entity meta classes have.
  The value ``none`` means the prefix is not used.
  The default value is ``$``.

doma.expr.functions
  The full qualified name of the class that implements ``org.seasar.doma.expr.ExpressionFunctions``.
  The default value is ``org.seasar.doma.expr.ExpressionFunctions``.
  This value are used to determine which functions are available in expression comments.

doma.metamodel.enabled
  Whether to generate meta classes for the Criteria API.
  When the value is ``true``, metamodels are generated for all entity classes
  even if they are not specified with ``metamodel = @Metamodel``.
  The default value is ``false``.

doma.metamodel.prefix
  The name prefix of the metamodel classes for the Criteria API.
  The default value is an empty string.

doma.metamodel.suffix
  The name suffix of the metamodel classes for the Criteria API.
  The default value is ``_``.

doma.resources.dir
  The resource directory that contains the resource files such as a doma.compile.config file and sql files.
  Specify the value as an absolute path.
  If the value is not specified, the resource directory is same as the directory the classes are generated.

doma.sql.validation
  Whether to validate the existence of sql files and the grammar of sql comments.
  If the value is ``true``, the validations run.
  To disable the validations, set ``false``.
  The default value is ``true``.

doma.version.validation
  Whether to validate the versions of doma.jar between runtime and compile-time.
  If the value is ``true``, the validation runs.
  To disable the validation, set ``false``.
  The default value is ``true``.

doma.config.path
  The file path of the configuration file for Doma.
  The default value is ``doma.compile.config``.

Setting options in Gradle
=========================

Use `the compilerArgs property
<https://docs.gradle.org/5.0/dsl/org.gradle.api.tasks.compile.CompileOptions.html#org.gradle.api.tasks.compile.CompileOptions:compilerArgs>`_:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            tasks {
                compileJava {
                    options.compilerArgs.addAll(listOf("-Adoma.dao.subpackage=impl", "-Adoma.dao.suffix=Impl"))
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy

            compileJava {
                options {
                    compilerArgs = ['-Adoma.dao.subpackage=impl', '-Adoma.dao.suffix=Impl']
                }
            }

Setting options in Maven
=========================

Use `the compilerArgs parameter
<https://maven.apache.org/plugins/maven-compiler-plugin/examples/pass-compiler-arguments.html>`_:

.. code-block:: xml

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source> <!-- depending on your project -->
                    <target>17</target> <!-- depending on your project -->
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.seasar.doma</groupId>
                            <artifactId>doma-processor</artifactId>
                            <version>${doma.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <arg>-Adoma.resources.dir=${project.basedir}/src/main/resources</arg>
                        <arg>-Adoma.dao.subpackage=impl</arg>
                        <arg>-Adoma.dao.suffix=Impl</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>

Setting options in Eclipse
==========================

Gradle
~~~~~~

Use the Gradle plugin `com.diffplug.eclipse.apt
<https://plugins.gradle.org/plugin/com.diffplug.eclipse.apt>`_
and the ``processorArgs`` property:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            plugins {
                id("com.diffplug.eclipse.apt") version "3.43.0"
            }

            tasks {
                compileJava {
                    val aptOptions = extensions.getByType<com.diffplug.gradle.eclipse.apt.AptPlugin.AptOptions>()
                    aptOptions.processorArgs = mapOf(
                        "doma.dao.subpackage" to "impl",
                        "doma.dao.suffix" to "Impl"     
                    )
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy
        
            plugins {
                id 'com.diffplug.eclipse.apt' version '3.43.0'
            }
            
            compileJava {
                aptOptions {
                    processorArgs = [
                        'doma.dao.subpackage' : 'impl', 'doma.dao.suffix' : 'Impl'
                    ]
                }
            }

Right-click on the project in Eclipse and select Gradle > Refresh Gradle Project.
This will reflect the Gradle annotation processing options in Eclipse.

Maven
~~~~~

Right-click on the project in Eclipse and select Maven > Update Project....
This will reflect the Maven annotation processing options in Eclipse.

Setting options in IntelliJ IDEA
================================

Gradle
~~~~~~

Import your project as a Gradle project.
In the case, the options written in build.gradle(.kts) are used.

Maven
~~~~~

Import your project as a Maven project.
In the case, the options written in pom.xml are used.

Setting options with configuration file
=======================================

The options specified in the ``doma.compile.config`` file are available in all build tools
such as Eclipse, IDEA, Gradle and so on.

The ``doma.compile.config`` file must follow the properties file format
and be placed in the root directory such as ``src/main/resources``.

.. note::
  The options specified in the ``doma.compile.config`` file are overridden by
  the ones specific to the build tools.
