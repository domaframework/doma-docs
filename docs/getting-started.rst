===============
Getting started
===============

.. contents::
   :depth: 3

Summary
========

This guide outlines the setup of your development environment and introduces basic functionalities.

Install JDK
============

To begin, JDK 17 is required. 

.. note::

  It's important to note that Doma is compatible with JDK 8 onwards. 
  For details on supported JDK versions, refer to the appropriate section.
  :ref:`Which version of JDK does Doma support?<which-version-of-jdk-does-doma-support>`.

Get sample project
==================

To obtain the sample project, clone the `getting-started <https://github.com/domaframework/getting-started>`_ 
repository and navigate to the new directory using the following commands:

.. code-block:: bash

  $ git clone https://github.com/domaframework/getting-started.git
  $ cd getting-started

Ensure successful project setup with:

.. code-block:: bash

  $ ./gradlew build

.. note::

  For Windows users, execute ``gradlew build``.

Sample project structure
========================

The getting-started sample is a Gradle multi-project consisting of java-8 and java-17 sub-projects. 
Both are similar, with the primary difference being how SQL statements are stored: in files for java-8 and as Text Blocks in java-17.

This guide will focus on the java-17 project.

Import project to your IDE
==========================

Eclipse
-------

Tested on Eclipse 4.23.0.
Import the getting-started project as a Gradle project.

.. note::

  If you want to store SQL statements in files, 
  `Doma Tools <https://github.com/domaframework/doma-tools>`_ can help you.

IntelliJ IDEA
-------------

Tested with IntelliJ IDEA Community 2023.3.4.
Import the getting-started project as a Gradle project. 

.. note::

  If you use IntelliJ IDEA Ultimate Edition,
  `Doma Support <https://plugins.jetbrains.com/plugin/7615-doma-support>`_ can help you.

Programming styles
==================

Doma supports two programming styles: DSL and DAO. 

The DSL style utilizes the Criteria API for building type-safe SQL statements, offering several benefits, 
such as not requiring reflection and supporting various types of associations (one-to-many, many-to-one, one-to-one). 

The DAO style, on the other hand, maps SQL statements to Java interface methods. 

It's recommended to use the DSL style due to the Criteria API's advantages.

DSL style
=========

In the DSL style, you work with examples in the ``boilerplate.java17.repository.EmployeeRepository`` 
and the :doc:`criteria-api` for operations. 

SELECT
------

To execute a SELECT query and retrieve Java object results, follow this example:

.. code-block:: java

  public Employee selectById(Integer id) {
    var e = new Employee_();
    return entityql.from(e).where(c -> c.eq(e.id, id)).fetchOne();
  }

You'll use a metamodel class, like ``Employee_`` for ``Employee```, which is auto-generated through annotation processing. 

The ``entityql`` instance from the ``Entityql`` class serves as the Criteria API's starting point. 

The above code generates the following SQL statement:

.. code-block:: sql

    select t0_.id, t0_.name, t0_.age, t0_.version from Employee t0_ where t0_.id = ?

DELETE
------

To issue a DELETE statement, write as follows:

.. code-block:: java

  public void delete(Employee employee) {
    var e = new Employee_();
    entityql.delete(e, employee).execute();
  }

INSERT
------

To issue an INSERT statement, write as follows:

.. code-block:: java

  public void insert(Employee employee) {
    var e = new Employee_();
    entityql.insert(e, employee).execute();
  }

UPDATE
------

To issue an UPDATE statement, write as follows:

.. code-block:: java

  public void update(Employee employee) {
    var e = new Employee_();
    entityql.update(e, employee).execute();
  }

DAO style
=========

You can find some examples in ``boilerplate.java17.dao.EmployeeDao``.
See :doc:`dao` and :doc:`sql` for more information.

SELECT (DAO)
------------

In the DAO style, for issuing a SELECT statement to retrieve Java objects, 
use the ``@Sql`` annotation with Text Blocks for SQL templates:

.. code-block:: java

    @Sql("""
        select
          /*%expand*/*
        from
          employee
        where
          id = /* id */0
        """)
    @Select
    Employee selectById(Integer id);

This SQL template contains two special expressions, ``/*%expand*/`` and ``/* id */``.
In process of SQL template, ``/*%expand*/`` and the following ``*`` are replaced with column list.
And ``/* id */`` and the following ``0`` are replaced with the bind variable ``?``.
The bound value is the ``id`` parameter of the ``selectById`` method.

The above code generates the following SQL statement:

.. code-block:: sql

    select
      id, name, age, version
    from
      employee
    where
      id = ?

DELETE (DAO)
------------

To issue a DELETE statement, write as follows:

.. code-block:: java

    @Delete
    int delete(Employee employee);

INSERT (DAO)
------------

To issue an INSERT statement, write as follows:

.. code-block:: java

    @Insert
    int insert(Employee employee);

UPDATE (DAO)
------------

To issue an UPDATE statement, write as follows:

.. code-block:: java

    @Update
    int update(Employee employee);

Next Step
=========

See other example projects:

- `simple-examples <https://github.com/domaframework/simple-examples>`_
