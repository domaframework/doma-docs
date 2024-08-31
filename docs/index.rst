.. Doma documentation master file, created by
   sphinx-quickstart on Thu Feb 13 12:43:15 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

.. image:: images/doma.png
   :height: 200px
   :width: 200px
   :align: right
   :target: https://github.com/domaframework/doma

Welcome to Doma
=====================

Doma is a database access framework for Java.
Doma has various strengths:

* Verifies and generates source code at compile time using annotation processing.
* Provides type-safe Criteria API.
* Supports Kotlin.
* Uses SQL templates, called "two-way SQL".
* Has no dependence on other libraries.

This document consists of following sections:

* `User Documentation`_
* `About Doma`_
* `Links`_

.. admonition:: Please help with the improvement and localization of our documentation.
  :class: important

  For matters related to the English version of the documentation and general issues concerning the documentation, 
  please add issues or pull requests to `the GitHub repository <https://github.com/domaframework/doma-docs>`_.

  For localization into Japanese, please perform translations and voting 
  through `the project on Crowdin <https://crowdin.com/project/doma-docs>`_.

User Documentation
==================

.. toctree::
   :maxdepth: 2

   getting-started
   config
   basic
   domain
   embeddable
   entity
   dao
   query/index
   query-builder/index
   criteria-api
   sql
   expression
   transaction
   build
   annotation-processing
   lombok-support
   kotlin-support
   slf4j-support
   jpms-support
   quarkus-support
   codegen

About Doma
==========

.. toctree::
   :maxdepth: 1

   faq

Links
=====

* `News and announcements <https://twitter.com/domaframework>`_
* `GitHub repository <https://github.com/domaframework/doma>`_
* `Release Notes <https://github.com/domaframework/doma/releases>`_
* `JavaDoc <https://www.javadoc.io/doc/org.seasar.doma/doma-core/latest/index.html>`_
* `Examples <https://github.com/domaframework/simple-examples>`_
* `Doma Compile Plugin <https://github.com/domaframework/doma-compile-plugin>`_
* `Doma CodeGen Plugin <https://github.com/domaframework/doma-codegen-plugin>`_
* `Doma version 1 <http://doma.seasar.org/>`_
