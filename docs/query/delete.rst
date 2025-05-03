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

Optimistic concurrency control is executed if you satisfied below conditions.

* Entity class within parameter has property that is annotated with @Version
* The ignoreVersion element within @Delete annotation is false

If optimistic concurrency control is enable, version number is included with identifier in delete condition.
``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if at that time delete count is 0.

ignoreVersion
~~~~~~~~~~~~~

If the ``ignoreVersion`` property of the ``@Delete`` annotation is set to ``true``, the version number is not included in the delete condition.
In this case, ``OptimisticLockException`` is not thrown even if no rows are deleted.

.. code-block:: java

  @Delete(ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If ``suppressOptimisticLockException`` property within ``@Delete`` is ``true``, version number is included in delete condition.
But in this case ``OptimisticLockException`` is not thrown even if delete count is 0.

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

Entity listener method is not called even if the entity listener is specified to entity.

.. code-block:: java

  @Delete(sqlFile = true)
  int delete(Employee employee);

For example, you describe SQL file like below to correspond above method.

.. code-block:: sql

  delete from employee where name = /* employee.name */'hoge'

Version number and optimistic concurrency control in  SQL File
--------------------------------------------------------------

Optimistic concurrency control is performed when the following conditions are met:

* Entity class is included in parameter
* Entity class at first from the left within parameter has property that is annotated with @Version
* The ignoreVersion property within @Delete annotation is false
* The suppressOptimisticLockException property within @Delete annotation is false

However, describing to SQL file for Optimistic concurrency control SQL is application developer's responsibility.
For example like below SQL, you must specify version number in WHERE clauses.

.. code-block:: sql

  delete from EMPLOYEE where ID = /* employee.id */1 and VERSION = /* employee.version */1

``OptimisticLockException`` representing optimistic concurrency control failure is thrown, if this SQL delete count is 0.
``OptimisticLockException`` is not thrown if delete count is not 0.

ignoreVersion
~~~~~~~~~~~~~

If ``ignoreVersion`` property within ``@Delete`` annotation is ``true``,
``OptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, ignoreVersion = true)
  int delete(Employee employee);

suppressOptimisticLockException
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If ``suppressOptimisticLockException`` property within ``@Delete`` annotation is ``true``,
``OptimisticLockException`` is not thrown even if delete count is 0.

.. code-block:: java

  @Delete(sqlFile = true, suppressOptimisticLockException = true)
  int delete(Employee employee);

Query timeout
==================


You can specify seconds of query timeout to ``queryTimeout`` property within ``@Delete`` annotation.

.. code-block:: java

  @Delete(queryTimeout = 10)
  int delete(Employee employee);

This specifying is applied regardless of with or without using sql file.
Query timeout that is specified in :doc:`../config` is used if ``queryTimeout`` property is not set value.

SQL log output format
=====================

You can specify SQL log output format to ``sqlLog`` property within ``@Delete`` annotation.

.. code-block:: java

  @Delete(sqlLog = SqlLogType.RAW)
  int delete(Employee employee);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
