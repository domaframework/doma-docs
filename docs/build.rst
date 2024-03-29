=======================
Building an application
=======================

.. contents::
   :depth: 3

Maven Central Repository
========================

You can pull the artifacts of the Doma framework from the Maven central repository.
We provide two artifacts, ``doma-core`` and ``doma-processor``.

The ``doma-core`` artifact is required at runtime and compile-time.
The ``doma-processor`` artifact provides annotation processors and is required at compile-time only.

The group id and artifact id of those artifacts are as follows:

:GroupId: org.seasar.doma
:ArtifactId: doma-core

:GroupId: org.seasar.doma
:ArtifactId: doma-processor

.. _build-with-gradle:

Build with Gradle
=================

Write your build.gradle(.kts) as follows:

.. tabs::

    .. tab:: Kotlin
    
        .. code-block:: kotlin

            plugins {
                id("org.seasar.doma.compile") version "2.0.0"
            }
            
            dependencies {
                implementation("org.seasar.doma:doma-core:2.58.0")
                annotationProcessor("org.seasar.doma:doma-processor:2.58.0")
            }

    .. tab:: Groovy

        .. code-block:: groovy

            plugins {
                id 'org.seasar.doma.compile' version '2.0.0'
            }
            
            dependencies {
                implementation 'org.seasar.doma:doma-core:2.58.0'
                annotationProcessor 'org.seasar.doma:doma-processor:2.58.0'
            }

To simplify your build.script(.kts), we recommend that you use the `org.seasar.doma.compile`_ plugin.

See build.gradle.kts in the `getting-started`_ repository as an example.

.. _build-with-maven

Build with Maven
=================

Write your pom.xml as follows:

.. code-block:: xml

    ...
    <properties>
        <doma.version>2.58.0</doma.version>
    </properties>
    ...
    <dependencies>
        <dependency>
            <groupId>org.seasar.doma</groupId>
            <artifactId>doma-core</artifactId>
            <version>${doma.version}</version>
        </dependency>
    </dependencies>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source> <!-- depending on your project -->
                    <target>1.8</target> <!-- depending on your project -->
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.seasar.doma</groupId>
                            <artifactId>doma-processor</artifactId>
                            <version>${doma.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>

.. _build-with-eclipse:

Build with Eclipse
==================

Gradle
------

Generate eclipse setting files with Gradle, and then import your project into Eclipse.
To generate the setting files, run ``gradle cleanEclipse eclipse``.

To simplify your build.script(.kts), we recommend that you use the `com.diffplug.eclipse.apt`_ plugin.

See build.gradle.kts in the `getting-started`_ repository as an example.

Maven
------

.. note::

  We've managed to get our Maven project up and running in Eclipse, but we're unsure if we're following the best practices. 
  If you have any tips or recommended approaches, we'd really appreciate your input.

.. _build-with-idea:

Build with IntelliJ IDEA
========================

Gradle
------

Import your project as a Gradle project.
Build and run using Gradle.

Maven
------

Import your project as a Maven project.
Build and run using Maven.


.. _org.seasar.doma.compile: https://github.com/domaframework/doma-compile-plugin
.. _com.diffplug.eclipse.apt: https://plugins.gradle.org/plugin/com.diffplug.eclipse.apt
.. _getting-started: https://github.com/domaframework/getting-started
