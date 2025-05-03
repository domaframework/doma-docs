==================
Batch update
==================

.. contents::
   :depth: 3

Annotate a Dao method with ``@BatchUpdate`` to execute batch update operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @BatchUpdate
      int[] update(List<Employee> employees);

      @BatchUpdate
      BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);
  }

By default, the UPDATE statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@BatchUpdate`` annotation.

If an entity listener is specified for the entity class, its ``preUpdate`` method is called for each entity before executing the update operation.
Similarly, the ``postUpdate`` method is called for each entity after the update operation completes.

Return value
=============

If the elements of the parameter (which must be an ``Iterable`` subtype) are immutable entity classes, the return value must be ``org.seasar.doma.jdbc.BatchResult`` with the entity class as its element type.

If the above condition is not met, the return value must be ``int[]``, where each element represents the number of rows affected by each update operation.

.. _auto-batch-update:

Batch update by auto generated SQL
===================================

The parameter type must be a subtype of ``java.lang.Iterable`` with entity classes as its elements.
Only one parameter can be specified.
The parameter must not be ``null``.
The number of elements in the return value array will equal the number of elements in the ``Iterable``.
Each element in the array represents the number of rows affected by the corresponding update operation.

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------

Optimistic concurrency control is executed if the following conditions are met:

* The :doc:`../entity` within the parameter's java.lang.Iterable subtype has a property that is annotated with @Version
* The ignoreVersion element within the @BatchUpdate annotation is false

When optimistic concurrency control is enabled, the version number is included with the identifier in the update condition and is incremented by 1.
If the update count is 0, a ``BatchOptimisticLockException`` is thrown, indicating an optimistic concurrency control failure.
If the update count is 1, the version property in the entity is incremented by 1 and no exception is thrown.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property of the ``@BatchUpdate`` annotation is set to ``true``,
the version number is not included in the update condition but is included in the SET clauses of the UPDATE statement.
The version number is updated with the value set in the application.
In this case, ``BatchOptimisticLockException`` is not thrown even if the update count is 0.

.. code-block:: java

  @BatchUpdate(ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the ``suppressOptimisticLockException`` property within the ``@BatchUpdate`` annotation is ``true``,
and a property annotated with ``@Version`` exists, then the version number is included in the update condition and is incremented by 1.
However, a ``BatchOptimisticLockException`` is not thrown even if the update count is 0.
In this case, the version property value within the entity is still incremented by 1.

.. code-block:: java

  @BatchUpdate(suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

Update target property
----------------------

updatable
~~~~~~~~~

A property with its ``updatable`` property in the ``@Column`` annotation set to ``false`` is excluded from the update operation if the :doc:`../entity` has a property that is annotated with ``@Column``.

exclude
~~~~~~~

Properties specified in the ``exclude`` property of the ``@BatchUpdate`` annotation are excluded from the update operation.
Even if the ``updatable`` property of the ``@Column`` annotation is set to ``true``, a property will be excluded from the update if it is listed in the ``exclude`` property.

.. code-block:: java

  @BatchUpdate(exclude = {"name", "salary"})
  int[] update(List<Employee> employees);

include
~~~~~~~

Only properties that are specified in the ``include`` property of the ``@BatchUpdate`` annotation are included in the update operation.
If the same property is specified in both the ``include`` property and the ``exclude`` property of the ``@BatchUpdate`` annotation, the property is excluded from the update operation.
Even if a property is specified in this element, it is excluded from the update operation if the ``updatable`` property in the ``@Column`` annotation is ``false``.

.. code-block:: java

  @BatchUpdate(include = {"name", "salary"})
  int[] update(List<Employee> employees);

Batch update by SQL file
=========================

To execute batch updates using an SQL file,
set the ``sqlFile`` property to ``true`` in the ``@BatchUpdate`` annotation and prepare an SQL file that corresponds to the method.

.. note::

  In batch updates using SQL files, the rules differ depending on whether you use :ref:`populate` or not.

Case of using a comment that generates the update column list
---------------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

The parameter type must be a ``java.lang.Iterable`` subtype that has :doc:`../entity` as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The update count is returned in each element of the array.

For example, you would write an SQL file like the one below to correspond to the above method.

.. code-block:: sql

  update employee set /*%populate*/ id = id where name = /* employees.name */'hoge'

The parameter name indicates the ``Iterable`` subtype element in the SQL file.

The rules about update target properties are the same as in :ref:`auto-batch-update`.

Case of not using a comment that generates the update column list
------------------------------------------------------------

.. code-block:: java

  @BatchUpdate(sqlFile = true)
  int[] update(List<Employee> employees);

  @BatchUpdate
  BatchResult<ImmutableEmployee> update(List<ImmutableEmployee> employees);

The parameter type must be a ``java.lang.Iterable`` subtype that has an arbitrary type as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The update count is returned in each element of the array.

For example, you would write an SQL file like the one below to correspond to the above method.

.. code-block:: sql

  update employee set name = /* employees.name */'hoge', salary = /* employees.salary */100
  where id = /* employees.id */0

The parameter name indicates the ``Iterable`` subtype element in the SQL file.

Automatic version number updating is not executed in batch updates using SQL files.
Also, the ``exclude`` property and the ``include`` property of the ``@BatchUpdate`` annotation are not referenced.

Version number and optimistic concurrency control in SQL file
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Optimistic concurrency control is performed when the following conditions are met:

* The java.lang.Iterable subtype element in the parameter is a :doc:`../entity`
  and has a property that is annotated with @Version.
* The ignoreVersion property within the @BatchUpdate annotation is false.

However, writing SQL for optimistic concurrency control is the application developer's responsibility.
For example, in the SQL below, you must specify the version number in the WHERE clause and increment the version number by 1 in the SET clause.

.. code-block:: sql

  update EMPLOYEE set DELETE_FLAG = 1, VERSION = /* employees.version */1 + 1
  where ID = /* employees.id */1 and VERSION = /* employees.version */1

A ``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown if this SQL update count is 0.
A ``BatchOptimisticLockException`` is not thrown and the version property within the entity is incremented by 1 if the update count is not 0.

If optimistic concurrency control is enabled, the version number is included with the identifier in the update condition and is incremented by 1.
A ``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown if the update count is 0.
On the other hand, if the update count is 1, a ``BatchOptimisticLockException`` is not thrown and the entity version property is incremented by 1.

ignoreVersion
^^^^^^^^^^^^^

If the ``ignoreVersion`` property within the ``@BatchUpdate`` annotation is true,
a ``BatchOptimisticLockException`` is not thrown, even if the update count is 0 or multiple.
Also, the entity version property is not modified.

.. code-block:: java

  @BatchUpdate(sqlFile = true, ignoreVersion = true)
  int[] update(List<Employee> employees);

suppressOptimisticLockException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the ``suppressOptimisticLockException`` property within the ``@BatchUpdate`` annotation is ``true``,
a ``BatchOptimisticLockException`` is not thrown even if the update count is 0.
However, the entity version property value is still incremented by 1.

.. code-block:: java

  @BatchUpdate(sqlFile = true, suppressOptimisticLockException = true)
  int[] update(List<Employee> employees);

Unique constraint violation
============================

A ``UniqueConstraintException`` is thrown regardless of whether you are using an SQL file or not if a unique constraint violation occurs.

Query timeout
==================

You can specify the number of seconds for query timeout in the ``queryTimeout`` property of the ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(queryTimeout = 10)
  int[] update(List<Employee> employees);

This specification is applied regardless of whether you are using an SQL file or not.
The query timeout that is specified in the config class is used if the ``queryTimeout`` property is not set.

Batch size
============

You can specify the batch size in the ``batchSize`` property of the ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(batchSize = 10)
  int[] update(List<Employee> employees);

This specification is applied regardless of whether you are using an SQL file or not.
If you do not specify a value for the ``batchSize`` property, the batch size that is specified in the :doc:`../config` class is used.

SQL log output format
======================

You can specify the SQL log output format in the ``sqlLog`` property of the ``@BatchUpdate`` annotation.

.. code-block:: java

  @BatchUpdate(sqlLog = SqlLogType.RAW)
  int[] update(List<Employee> employees);

``SqlLogType.RAW`` represents outputting a log that contains SQL with binding parameters.
