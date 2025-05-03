==================
Batch delete
==================

.. contents::
   :depth: 3

Annotate a Dao method with ``@BatchDelete`` to execute batch delete operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @BatchDelete
      int[] delete(List<Employee> employees);
      ...
  }

By default, the DELETE statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@BatchDelete`` annotation.

The ``preDelete`` method of the entity listener is called for each entity before executing the delete operation if an entity listener is specified for the :doc:`../entity` parameter.
Similarly, the ``postDelete`` method of the entity listener is called for each entity after the delete operation completes.

Return value
==============

The return value must be ``org.seasar.doma.jdbc.BatchResult`` with the entity class as its element type if the elements of the parameter (which must be an ``Iterable`` subtype) are immutable entity classes.

The return value must be ``int[]``, where each element represents the number of rows affected by each delete operation, if the above condition is not met.

Batch delete by auto generated SQL
====================================

The parameter type must be a ``java.lang.Iterable`` subtype that has :doc:`../entity` as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The delete count is returned in each element of the array.

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------------

Optimistic concurrency control is executed if the following conditions are met:

* The :doc:`../entity` within the parameter's java.lang.Iterable subtype has a property that is annotated with @Version
* The ignoreVersion element within the @BatchDelete annotation is false

If optimistic concurrency control is enabled, the version number is included with the identifier in the delete condition.
A ``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown if the delete count is 0.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property within the ``@BatchDelete`` annotation is ``true``, the version number is not included in the delete condition.
A ``BatchOptimisticLockException`` is not thrown, even if the delete count is 0.

.. code-block:: java

  @BatchDelete(ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the ``suppressOptimisticLockException`` property within the ``@BatchDelete`` annotation is ``true``,
the version number is included in the delete condition but a ``BatchOptimisticLockException`` is not thrown even if the delete count is 0.

.. code-block:: java

  @BatchDelete(suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

Batch delete by SQL file
===========================

To execute batch deletes using an SQL file,
set the ``sqlFile`` property to ``true`` in the ``@BatchDelete`` annotation and prepare an SQL file that corresponds to the method.

.. code-block:: java

  @BatchDelete(sqlFile = true)
  int[] delete(List<Employee> employees);

The parameter type must be a ``java.lang.Iterable`` subtype that has an arbitrary type as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The delete count is returned in each element of the array.

For example, you would write an SQL file like the one below to correspond to the above method.

.. code-block:: sql

  delete from employee where name = /* employees.name */'hoge'

The parameter name indicates the ``java.lang.Iterable`` subtype element in the SQL file.

Version number and optimistic concurrency control in SQL file
--------------------------------------------------------------

Optimistic concurrency control is executed if the following conditions are met:

* The parameter's ``java.lang.Iterable`` subtype has a :doc:`../entity` element, and the :doc:`../entity` element has a property annotated with @Version
* The ignoreVersion element within the @BatchDelete annotation is false

However, writing SQL for optimistic concurrency control is the application developer's responsibility.
For example, in the SQL below, you must specify the version number in the WHERE clause.

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employees.id */1 and VERSION = /* employees.version */1

A ``BatchOptimisticLockException`` representing optimistic concurrency control failure is thrown if the delete count is 0 or multiple in this SQL.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property within the ``@BatchDelete`` annotation is true,
a ``BatchOptimisticLockException`` is not thrown even if the delete count is 0 or multiple.

.. code-block:: java

  @BatchDelete(sqlFile = true, ignoreVersion = true)
  int[] delete(List<Employee> employees);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the ``suppressOptimisticLockException`` property within the ``@BatchDelete`` annotation is ``true``,
a ``BatchOptimisticLockException`` is not thrown even if the delete count is 0 or multiple.

.. code-block:: java

  @BatchDelete(sqlFile = true, suppressOptimisticLockException = true)
  int[] delete(List<Employee> employees);

Query timeout
==================

You can specify the number of seconds for query timeout in the ``queryTimeout`` property of the ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(queryTimeout = 10)
  int[] delete(List<Employee> employees);

This specification is applied regardless of whether you are using an SQL file or not.
The query timeout that is specified in the config class is used if the ``queryTimeout`` property is not set.

Batch size
============

You can specify the batch size in the ``batchSize`` property of the ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(batchSize = 10)
  int[] delete(List<Employee> employees);

This specification is applied regardless of whether you are using an SQL file or not.
If you do not specify a value for the ``batchSize`` property, the batch size that is specified in the :doc:`../config` class is used.

SQL log output format
=======================

You can specify the SQL log output format in the ``sqlLog`` property of the ``@BatchDelete`` annotation.

.. code-block:: java

  @BatchDelete(sqlLog = SqlLogType.RAW)
  int[] delete(List<Employee> employees);

``SqlLogType.RAW`` outputs the SQL statement with its binding parameters in the log.
