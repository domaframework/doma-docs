===============
Quarkus support
===============

.. contents::
   :depth: 3

Overview
========

Doma supports `Quarkus <https://quarkus.io/>`_ v3.7.0 and later.
To combine Doma with Quarkus, the `quarkus-doma <https://github.com/quarkiverse/quarkus-doma>`_ module is necessary.

.. note::

  Quarkus and the quarkus-doma module require Java 17 and later.

Installing
==========

Gradle
------

.. code-block:: kotlin

    dependencies {
        annotationProcessor("org.seasar.doma:doma-processor:2.58.0")
        implementation("org.seasar.doma:doma-core:2.58.0")
        implementation("io.quarkiverse.doma:quarkus-doma:0.0.9")
    }

Maven
-----

.. code-block:: xml

    ...
    <properties>
        <doma.version>2.58.0</doma.version>
        <quarkus-doma.version>0.0.9</quarkus-doma.version>
        <compiler-plugin.version>3.9.0</compiler-plugin.version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>org.seasar.doma</groupId>
            <artifactId>doma-core</artifactId>
            <version>${doma.version}</version>
        </dependency>
        <dependency>
            <groupId>io.quarkiverse.doma</groupId>
            <artifactId>quarkus-doma</artifactId>
            <version>${quarkus-doma.version}</version>
        </dependency>
    </dependencies>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${compiler-plugin.version}</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <!-- the parameters=true option is critical so that RESTEasy works fine -->
                    <parameters>true</parameters>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.seasar.doma</groupId>
                            <artifactId>doma-processor</artifactId>
                            <version>${doma.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <!-- if you are using a Maven project in Eclipse, this argument is required -->
                        <arg>-Adoma.resources.dir=${project.basedir}/src/main/resources</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>

Main features
=============

Hot reloading
-------------

In development mode, the quarkus-doma module reloads SQL and Script files when they are changed.

Automatic bean register
-----------------------

The quarkus-doma module registers all DAO beans to the Quarkus CDI container.

Automatic SQL execution on startup
-----------------------------------

The quarkus-doma module executes import.sql when Quarkus starts.

Configuration
-------------

You can write the following configurations in your application.properties file:

.. code-block:: properties

    quarkus.doma.sql-file-repository=greedy-cache
    quarkus.doma.naming=none
    quarkus.doma.exception-sql-log-type=none
    quarkus.doma.dialect=h2
    quarkus.doma.batch-size=10
    quarkus.doma.fetch-size=50
    quarkus.doma.max-rows=500
    quarkus.doma.query-timeout=5000
    quarkus.doma.sql-load-script=import.sql

The above properties are all optional.

Please refer to the `Configuration References <https://docs.quarkiverse.io/quarkus-doma/dev/index.html#_configuration_references>`_ for details.

Multiple Datasources
--------------------

You can bind Doma’s configurations to each datasource as follows:

.. code-block:: properties

    # default datasource
    quarkus.datasource.db-kind=h2
    quarkus.datasource.username=username-default
    quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:default
    quarkus.datasource.jdbc.min-size=3
    quarkus.datasource.jdbc.max-size=13
    
    # inventory datasource
    quarkus.datasource.inventory.db-kind=h2
    quarkus.datasource.inventory.username=username2
    quarkus.datasource.inventory.jdbc.url=jdbc:h2:tcp://localhost/mem:inventory
    quarkus.datasource.inventory.jdbc.min-size=2
    quarkus.datasource.inventory.jdbc.max-size=12
    
    # Doma's configuration bound to the above default datasource
    quarkus.doma.dialect=h2
    quarkus.doma.batch-size=10
    quarkus.doma.fetch-size=50
    quarkus.doma.max-rows=500
    quarkus.doma.query-timeout=5000
    quarkus.doma.sql-load-script=import.sql
    
    # Doma's configuration bound to the above inventory datasource
    quarkus.doma.inventory.dialect=h2
    quarkus.doma.inventory.batch-size=10
    quarkus.doma.inventory.fetch-size=50
    quarkus.doma.inventory.max-rows=500
    quarkus.doma.inventory.query-timeout=5000
    quarkus.doma.inventory.sql-load-script=import.sql

You can inject the named Doma’s resource using the ``io.quarkus.agroal.DataSource`` qualifier as follows:

.. code-block:: java

    @Inject
    Config defaultConfig;
    
    @Inject
    Entityql defaultEntityql;
    
    @Inject
    NativeSql defaultNativeSql;
    
    @Inject
    @DataSource("inventory")
    Config invetoryConfig;
    
    @Inject
    @DataSource("inventory")
    Entityql inventoryEntityql;
    
    @Inject
    @DataSource("inventory")
    NativeSql inventoryNativeSql;

Support for native images
-------------------------

The quarkus-doma module recognizes reflective classes and resources, and includes them into your native image without additional configurations.

Sample project
==============

`domaframework/quarkus-sample <https://github.com/domaframework/quarkus-sample>`_
