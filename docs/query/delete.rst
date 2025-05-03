==================
Delete
==================

.. contents::
   :depth: 3

Annotate a Dao method with ``@Delete`` to execute delete operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Delete
      int delete(Employee employee);
  }

By default, the DELETE statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@Delete`` annotation.

If an entity listener is specified for the entity class parameter, its ``preDelete`` method is called before executing the delete operation.
Similarly, the ``postDelete`` method is called after the delete operation completes.

Return value
============

When using the returning property
---------------------------------

See :ref:`delete-returning`.

When not using the returning property
-------------------------------------

If the parameter is an immutable entity class, the return value must be ``org.seasar.doma.jdbc.Result`` with the entity class as its element.

If the above condition is not met, the return value must be ``int``, which represents the number of rows affected by the delete operation.

Delete by auto generated SQL
=============================

The parameter type must be an entity class.
Only one parameter can be specified.
The parameter must not be ``null``.

.. code-block:: java

  @Delete
  int delete(Employee employee);

  @Delete
  Result<ImmutableEmployee> delete(ImmutableEmployee employee);

Version number and optimistic concurrency control in auto generated SQL
-----------------------------------------------------------------------

Optimistic concurrency control is executed if the following conditions are met:

* The entity class parameter has a property that is annotated with @Version
* The ignoreVersion element within the @Delete annotation is false

If optimistic concurrency control is enabled, the version number is included with the identifier in the delete condition.
An ``OptimisticLockException`` representing optimistic concurrency control failure is thrown if the delete count is 0.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property of the ``@Delete`` annotation is set to ``true``, the version number is not included in the delete condition.
In this case, ``OptimisticLockException`` is not thrown even if no rows are deleted.

.. code-block:: java

  @Delete(ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the ``suppressOptimisticLockException`` property within the ``@Delete`` annotation is ``true``, the version number is included in the delete condition.
However, in this case, an ``OptimisticLockException`` is not thrown even if the delete count is 0.

.. code-block:: java

  @Delete(suppressOptimisticLockException = true)
  int delete(Employee employee);

.. _delete-returning:

returning
~~~~~~~~~

By specifying ``@Returning`` in the ``returning`` property,
you can generate SQL equivalent to the ``DELETE .. RETURNING`` clause.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Delete(returning = @Returning)
      Employee delete(Employee employee);

      @Delete(returning = @Returning(include = { "employeeId", "version" }))
      Employee deleteReturningIdAndVersion(Employee employee);

      @Delete(returning = @Returning(exclude = { "password" }))
      Employee deleteReturningExceptPassword(Employee employee);

      @Delete(returning = @Returning, suppressOptimisticLockException = true)
      Optional<Employee> deleteOrIgnore(Employee employee);
  }

You can use the ``include`` property of ``@Returning`` to specify which entity properties
(corresponding to database columns) should be returned by the RETURNING clause.
Alternatively, you can use the ``exclude`` property to specify which properties should not be returned.
When both properties are specified, the ``exclude`` property takes precedence.

The return type must be either an entity class
or an ``Optional`` containing an entity class as its element.

.. note::

  Only H2 Database, PostgreSQL, SQL Server, and SQLite Dialects support this feature.


Delete by SQL file
===========================

To execute a delete operation using a SQL file, set the ``sqlFile`` property to ``true`` in the ``@Delete`` annotation and prepare a corresponding SQL file for the method.


You can use any type as a parameter.
There is no limit to the number of parameters you can specify.
You can pass ``null`` to parameters of basic type or domain class.
For other types, parameters must not be ``null``.

Entity listener methods are not called even if an entity listener is specified for the entity.

.. code-block:: java

  @Delete(sqlFile = true)
  int delete(Employee employee);

For example, you would write an SQL file like the one below to correspond to the above method.

.. code-block:: sql

  delete from employee where name = /* employee.name */'hoge'

Version number and optimistic concurrency control in  SQL File
--------------------------------------------------------------

Optimistic concurrency control is performed when the following conditions are met:

* An entity class is included in the parameters
* The leftmost entity class parameter has a property that is annotated with @Version
* The ignoreVersion property within the @Delete annotation is false
* The suppressOptimisticLockException property within the @Delete annotation is false

However, writing SQL for optimistic concurrency control is the application developer's responsibility.
For example, in the SQL below, you must specify the version number in the WHERE clause.

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employee.id */1 and VERSION = /* employee.version */1

An ``OptimisticLockException`` representing optimistic concurrency control failure is thrown if this SQL delete count is 0.
An ``OptimisticLockException`` is not thrown if the delete count is not 0.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property within the ``@Delete`` annotation is ``true``,
an ``OptimisticLockException`` is not thrown even if the delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the ``suppressOptimisticLockException`` property within the ``@Delete`` annotation is ``true``,
an ``OptimisticLockException`` is not thrown even if the delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, suppressOptimisticLockException = true)
  int delete(Employee employee);

Query timeout
==================


You can specify the number of seconds for query timeout in the ``queryTimeout`` property of the ``@Delete`` annotation.

.. code-block:: java

  @Delete(queryTimeout = 10)
  int delete(Employee employee);

This specification is applied regardless of whether you are using an SQL file or not.
The query timeout that is specified in :doc:`../config` is used if the ``queryTimeout`` property is not set.

SQL log output format
=====================

You can specify the SQL log output format in the ``sqlLog`` property of the ``@Delete`` annotation.

.. code-block:: java

  @Delete(sqlLog = SqlLogType.RAW)
  int delete(Employee employee);

``SqlLogType.RAW`` represents outputting a log that contains SQL with binding parameters.
