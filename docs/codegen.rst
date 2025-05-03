===================
Doma CodeGen Plugin
===================

.. contents::
   :depth: 4

Overview
========

The `Doma CodeGen Plugin <https://plugins.gradle.org/plugin/org.domaframework.doma.codegen>`_ is a Gradle plugin 
that generates Java, Kotlin, and SQL files from a database schema.

.. admonition:: Are you looking for documentation for Ant-based Doma-Gen?
    :class: important

    Documentation for Ant-based Doma-Gen is available at 
    `the Doma-Gen GitHub repository <https://github.com/domaframework/doma-gen/tree/master/docs>`_.

    Please note that Ant-based Doma-Gen is no longer maintained. We recommend using the Doma CodeGen Plugin described on this page instead.

How to use
====================

The basic build.gradle(.kts) example is as follows:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            buildscript {
                repositories {
                    mavenCentral()
                }
                dependencies {
                    // specify your JDBC driver
                    classpath("com.h2database:h2:2.3.232")
                }
            }
            
            plugins {
                id("java")
                id("org.domaframework.doma.codegen") version "{{ doma_codegen_version }}"
            }
            
            domaCodeGen {
                // make an arbitrary named block
                register("dev") {
                    // JDBC url
                    url.set("...")
                    // JDBC user
                    user.set("...")
                    // JDBC password
                    password.set("...")
                    // configuration for generated entity source files
                    entity {
                        packageName.set("org.example.entity")
                    }
                    // configuration for generated DAO source files
                    dao {
                        packageName.set("org.example.dao")
                    }
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy
        
            buildscript {
                repositories {
                    mavenCentral()
                }
                dependencies {
                    // specify your JDBC driver
                    classpath 'com.h2database:h2:2.3.232'
                }
            }
            
            plugins {
                id 'java'
                id 'org.domaframework.doma.codegen' version '{{ doma_codegen_version }}'
            }
            
            domaCodeGen {
                // make an arbitrary named block
                dev {
                    // JDBC url
                    url = '...'
                    // JDBC user
                    user = '...'
                    // JDBC password
                    password = '...'
                    // configuration for generated entity source files
                    entity {
                      packageName = 'org.example.entity'
                    }
                    // configuration for generated DAO source files
                    dao {
                      packageName = 'org.example.dao'
                    }
                }
            }

To generate all files, run `domaCodeGenDevAll` task:

.. code-block:: sh

    $ ./gradlew domaCodeGenDevAll

Gradle Tasks
====================

The Doma CodeGen Plugin provides the following tasks:

- domaCodeGenXxxAll - Generates all.
- domaCodeGenXxxDao - Generates DAO source files.
- domaCodeGenXxxDto - Reads ResultSet metadata and generate a DTO source file.
- domaCodeGenXxxEntity - Generates Entity source files.
- domaCodeGenXxxSql - Generates SQL files.
- domaCodeGenXxxSqlTest - Generates SQL test source files.

Note that the *Xxx* part in each task name is replaced with the block name defined under the ``domaCodeGen`` block.
In the usage example above, the *Dev* part corresponds to the ``dev`` block.

To check all defined task names, run the `tasks` task:

.. code-block:: sh

    $ ./gradlew tasks

Config Options
====================

named config
------------

A named configuration must be defined under the ``domaCodeGen`` block.  
You can choose any name for your configuration.  
Multiple configurations can be defined under the ``domaCodeGen`` block.  

In the following example, we define two configs - ``sales`` and ``account``:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            domaCodeGen {
                register("sales") {   
                    url.set("jdbc:h2:mem:sales")
                }
                register("account") {
                    url.set("jdbc:h2:mem:account")
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy
        
            domaCodeGen {
                sales {   
                    url = "jdbc:h2:mem:sales" 
                }
                account {
                    url = "jdbc:h2:mem:account" 
                }
            }

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - url
     - JDBC url
     - 
     - 
   * - user
     - JDBC user
     - 
     - 
   * - password
     - JDBC password
     - 
     - 
   * - dataSource
     - database data source
     - 
     - inferred by the url
   * - codeGenDialect
     - database dialect
     - 
     - inferred by the url
   * - catalogName
     - database catalog name
     - 
     - 
   * - schemaName
     - database schema name
     - 
     - 
   * - tableNamePattern
     - database table pattern (Regex)
     - 
     - ".*"
   * - ignoredTableNamePattern
     - database ignored table pattern (Regex)
     - 
     - ".*$.*"
   * - tableTypes
     - database table type
     - such as "TABLE", "VIEW", and so on
     - "TABLE"
   * - versionColumnNamePattern
     - database version column pattern (Regex)
     - 
     - "VERSION([_]?NO)?"
   * - languageType
     - language of generation code
     - [#]_ `LanguageType.JAVA`, `LanguageType.KOTLIN`
     - `LanguageType.JAVA`
   * - languageClassResolver
     - class resolver for language dedicated classes
     - 
     - depends on `languageType`
   * - templateEncoding
     - encoding for freeMarker template files
     - 
     - "UTF-8"
   * - templateDir
     - directory for user customized template files
     - 
     - 
   * - encoding
     - encoding for generated Java source files
     - 
     - "UTF-8"
   * - sourceDir
     - directory for generated Java source files
     - 
     - depends on `languageType`
   * - testSourceDir
     - directory for generated Java test source files
     - 
     - depends on `languageType`
   * - resourceDir
     - directory for generated SQL files
     - 
     - "src/main/resources"
   * - globalFactory
     - entry point to customize plugin behavior
     - 
     - [#]_ The instance of `GlobalFactory`

entity
------

The ``entity`` block must be defined within a named configuration:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            domaCodeGen {
                register("sales") {
                    entity {
                        useAccessor.set(false)
                    }
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy

            domaCodeGen {
                sales {
                    entity {
                        useAccessor = false
                    }
                }
            }


.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - where to overwrite generated entity files or not
     - 
     - `true`
   * - overwriteListener
     - allow to overwrite listeners or not
     - 
     - `false`
   * - superclassName
     - common superclass for generated entity classes
     - 
     - 
   * - listenerSuperclassName
     - common superclass for generated entity listener classes
     - 
     - 
   * - packageName
     - package name for generated entity class
     - 
     - "example.entity"
   * - generationType
     - generation type for entity identities
     - [#]_ enum value of `GenerationType`
     - 
   * - namingType
     - naming convention
     - [#]_ enum value of `NamingType`
     - 
   * - initialValue
     - initial value for entity identities
     - 
     - 
   * - allocationSize
     - allocation size for entity identities
     - 
     - 
   * - showCatalogName
     - whether to show catalog names or not
     - 
     - `false`
   * - showSchemaName
     - whether to show schema names or not
     - 
     - `false`
   * - showTableName
     - whether to show table names or not
     - 
     - `true`
   * - showColumnName
     - whether to show column names or not
     - 
     - `true`
   * - showDbComment
     - whether to show database comments or not
     - 
     - `true`
   * - useAccessor
     - whether to use accessors or not
     - 
     - `true`
   * - useListener
     - whether to use listeners or not
     - 
     - `true`
   * - useMetamodel
     - whether to use metamodels or not
     - 
     - `true`
   * - useMappedSuperclass
     - whether to use mapped superclasses or not
     - 
     - `true`
   * - originalStatesPropertyName
     - property to be annotated with `@OriginalStates`
     - 
     - 
   * - entityPropertyClassNamesFile
     - file used to resolve entity property classes
     - 
     - 
   * - prefix
     - prefix for entity classes
     - 
     - 
   * - suffix
     - suffix for entity classes
     - 
     -

dao
---

A ``dao`` block must be under a named config:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            domaCodeGen {
                register("sales") {
                    dao {
                        packageName.set("org.example.sales.dao")
                    }
                }
            }

    .. tab:: Groovy

        .. code-block:: groovy

            domaCodeGen {
                sales {
                    dao {
                        packageName = 'org.example.sales.dao'
                    }
                }
            }

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - whether to overwrite generated DAO files or not
     - 
     - ``false``
   * - packageName
     - package name for generated DAO classes
     - 
     - "example.dao"
   * - suffix
     - suffix for Dao classes
     - 
     - "Dao"
   * - configClassName
     - ``org.seasar.doma.jdbc.Config`` implemented class name. The name is used at @Dao
     - 
     - ``false``

sql
---

An ``sql`` block must be under a named config:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            domaCodeGen {
                register("sales") {
                    sql {
                        overwrite.set(false)
                    }
                }
            }


    .. tab:: Groovy

        .. code-block:: groovy

            domaCodeGen {
                sales {
                    sql {
                      overwrite = false
                    }
                }
            }

.. list-table::
   :widths: 25 25 25 25
   :header-rows: 1

   * - Option
     - Description
     - Values
     - Default
   * - overwrite
     - whether to overwrite generated sql files or not
     - 
     - ``true``

Customization
====================

Generating Kotlin code
----------------------

To generate Kotlin code, set the languageType option to ``LanguageType.KOTLIN`` as follows:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            import org.seasar.doma.gradle.codegen.desc.LanguageType
            
            ...
            
            domaCodeGen {
                register("dev") {
                    url.set("...")
                    user.set("...")
                    password.set("...")
                    languageType.set(LanguageType.KOTLIN)
                    entity {
                        packageName.set("org.example.entity")
                    }
                    dao {
                        packageName.set("org.example.dao")
                    }
                }
            }


    .. tab:: Groovy

        .. code-block:: groovy
 
            import org.seasar.doma.gradle.codegen.desc.LanguageType
            
            ...
            
            domaCodeGen {
                dev {
                    url = '...'
                    user = '...'
                    password = '...'
                    languageType = LanguageType.KOTLIN
                    entity {
                        packageName = 'org.example.entity'
                    }
                    dao {
                        packageName = 'org.example.dao'
                    }
                }
            }

Using custom template files
---------------------------

The default template files can be found in 
`the source code repository of the Doma CodeGen Plugin <https://github.com/domaframework/doma-codegen-plugin/tree/master/codegen/src/main/resources/org/seasar/doma/gradle/codegen/template>`_.  
These files include:

.. list-table::
   :widths: 33 33 33
   :header-rows: 1

   * - Template File
     - Data Model Class
     - Generated Files
   * - entity.ftl
     - org.seasar.doma.gradle.codege.desc.EntityDesc
     - entity source files
   * - entityListener.ftl
     - org.seasar.doma.gradle.codege.desc.EntityListenerDesc
     - entity listener source files
   * - dao.ftl
     - org.seasar.doma.gradle.codege.desc.DaoDesc
     - DAO source files
   * - sqlTest.ftl
     - org.seasar.doma.gradle.codege.desc.SqlTestDesc
     - test source files for SQL
   * - selectById.sql.ftl
     - org.seasar.doma.gradle.codege.desc.SqlDesc
     - SQL files
   * - selectByIdAndVersion.sql.ftl
     - org.seasar.doma.gradle.codege.desc.SqlDesc
     - SQL files

To create custom templates, copy the default files, modify their contents without changing the filenames,
and place them in the directory specified by the `templateDir` option.

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            domaCodeGen {
                register("dev") {
                    url.set("...")
                    user.set("...")
                    password.set("...")
                    // specify the directory including your custom template files
                    templateDir.set(file("$projectDir/template"))
                    entity {
                        packageName.set("org.example.entity")
                    }
                    dao {
                        packageName.set("org.example.dao")
                    }
                }
            }


    .. tab:: Groovy

        .. code-block:: groovy

            domaCodeGen {
                dev {
                    url = '...'
                    user = '...'
                    password = '...'
                    // specify the directory including your custom template files
                    templateDir = file("$projectDir/template")
                    entity {
                        packageName = 'org.example.entity'
                    }
                    dao {
                        packageName = 'org.example.dao'
                    }
                }
            }

The Doma CodeGen Plugin uses `Apache FreeMarker <https://freemarker.apache.org/>`_ to process the template files.

Sample Project
====================

- `kotlin-sample <https://github.com/domaframework/kotlin-sample>`_

Footnote
====================

.. [#] The FQN of ``LanguageType`` is ``org.seasar.doma.gradle.codegen.desc.LanguageType``
.. [#] The FQN of ``GlobalFactory`` is ``org.seasar.doma.gradle.codegen.GlobalFactory``
.. [#] The FQN of ``GenerationType`` is ``org.seasar.doma.gradle.codegen.desc.GenerationType``
.. [#] The FQN of ``NamingType`` is ``org.seasar.doma.gradle.codegen.NamingType``
