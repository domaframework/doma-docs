==================
Batch insert
==================

.. contents::
   :depth: 3

Annotate a Dao method with ``@BatchInsert`` to execute batch insert operations.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @BatchInsert
      int[] insert(List<Employee> employees);

      @BatchInsert
      BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
  }

By default, the INSERT statement is automatically generated.
You can map to an arbitrary SQL file by setting the ``sqlFile`` property to ``true`` in the ``@BatchInsert`` annotation.

If an entity listener is specified for the entity class, its ``preInsert`` method is called for each entity before executing the insert operation.
Similarly, the ``postInsert`` method is called for each entity after the insert operation completes.

Return value
=============

If the elements of the parameter (which must be an ``Iterable`` subtype) are immutable entity classes, the return value must be ``org.seasar.doma.jdbc.BatchResult`` with the entity class as its element type.

If the above condition is not met, the return value must be ``int[]``, where each element represents the number of rows affected by each insert operation.

Batch insert by auto generated SQL
=====================================

The parameter type must be a ``java.lang.Iterable`` subtype that has :doc:`../entity` as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The insert count is returned in each element of the array.

Identifier
-----------

If the identifier in :doc:`../entity` is annotated with ``@GeneratedValue``, the identifier is automatically generated and set.

Please refer to :ref:`identity-auto-generation` for cautionary points.

If you don't use auto-generated keys in your application, you can enable the `ignoreGeneratedKeys` flag.
This flag may improve performance.

.. code-block:: java

  @BatchInsert(ignoreGeneratedKeys = true)
  int[] insert(List<Employee> entities);

Version number
----------------

If a value that is explicitly set is over ``0``, then that value is used if :doc:`../entity` has a property that is annotated with ``@Version``.
If the value is not set or is less than ``0``, the value is automatically set to ``1``.

Properties of @BatchInsert
---------------------------

insertable
~~~~~~~~~~

A property with its ``insertable`` property in the ``@Column`` annotation set to ``false`` is excluded from insertion if the :doc:`../entity` has a property that is annotated with ``@Column``.

exclude
~~~~~~~

A property that is specified in the ``exclude`` property of the ``@BatchInsert`` annotation is excluded from insertion.
Even if the ``insertable`` property in the ``@Column`` annotation is set to ``true``, the property is excluded from insertion if it is specified in this element.

.. code-block:: java

  @BatchInsert(exclude = {"name", "salary"})
  int[] insert(List<Employee> employees);

include
~~~~~~~

Only properties that are specified in the ``include`` property of the ``@BatchInsert`` annotation are included in the insertion.
If the same property is specified in both the ``include`` property and the ``exclude`` property of the ``@BatchInsert`` annotation, the property is excluded from insertion.
Even if a property is specified in this element, it is excluded from insertion if the ``insertable`` property in the ``@Column`` annotation is ``false``.

.. code-block:: java

  @BatchInsert(include = {"name", "salary"})
  int[] insert(List<Employee> employees);

duplicateKeyType
~~~~~~~~~~~~~~~~

This property defines the strategy for handling duplicate keys during an insert operation.

It can take one of three values:

* ``DuplicateKeyType.UPDATE``: If a duplicate key is encountered, the existing row in the table will be updated.
* ``DuplicateKeyType.IGNORE``: If a duplicate key is encountered, the insert operation will be ignored, and no changes will be made to the table.
* ``DuplicateKeyType.EXCEPTION``: If a duplicate key is encountered, an exception will be thrown.

.. code-block:: java

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int[] insert(List<Employee> employees);

duplicateKeys
~~~~~~~~~~~~~

This property represents the keys that should be used to determine if a duplicate key exists. If the duplicate key exists, the operation will use the ``duplicateKeyType`` strategy to handle the duplicate key.

.. code-block:: java

  @BatchInsert(duplicateKeyType = DuplicateKeyType.UPDATE, duplicateKeys = {"employeeNo"})
  int[] insert(List<Employee> employees);

.. note::

  This property is only utilized when the ``duplicateKeyType`` strategy is either ``DuplicateKeyType.UPDATE`` or ``DuplicateKeyType.IGNORE``.

.. note::

  The MySQL dialect does not utilize this property.

Batch insert by SQL file
===========================

To execute batch inserting by SQL file,
you set ``true`` to ``sqlFile`` property within ``@BatchInsert`` annotation and prepare SQL file that correspond method.

.. code-block:: java

  @BatchInsert(sqlFile = true)
  int[] insert(List<Employee> employees);

  @BatchInsert(sqlFile = true)
  BatchResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);

The parameter type must be a ``java.lang.Iterable`` subtype that has :doc:`../entity` as its element type.
Only one parameter can be specified.
The parameter must not be ``null``.
The return value array element count equals the ``Iterable`` element count.
The insert count is returned in each element of the array.

If an entity listener is specified for the :doc:`../entity`, the entity listener methods are not called.

For example, you would write an SQL file like the one below to correspond to the above method.

.. code-block:: sql

  insert into employee (id, name, salary, version)
  values (/* employees.id */0, /* employees.name */'hoge', /* employees.salary */100, /* employees.version */0)

The parameter name indicates the ``java.lang.Iterable`` subtype element in the SQL file.

Automatic identifier setting and automatic version number setting are not executed in batch insert operations using SQL files.

Additionally, the following properties of ``@BatchInsert`` are not used:

* exclude
* include
* duplicateKeyType
* duplicateKeys

Unique constraint violation
============================

A ``UniqueConstraintException`` is thrown regardless of whether you are using an SQL file or not if a unique constraint violation occurs.

Query timeout
==================

You can specify the number of seconds for query timeout in the ``queryTimeout`` property of the ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(queryTimeout = 10)
  int[] insert(List<Employee> employees);

This specification is applied regardless of whether you are using an SQL file or not.
The query timeout that is specified in the config class is used if the ``queryTimeout`` property is not set.

Batch size
============

You can specify the batch size in the ``batchSize`` property of the ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(batchSize = 10)
  int[] insert(List<Employee> employees);

This setting applies regardless of whether you use a SQL file or not.
If you do not specify a value for the ``batchSize`` property, the batch size configured in the :doc:`../config` class is used.

SQL log output format
=====================

You can specify the SQL log output format in the ``sqlLog`` property of the ``@BatchInsert`` annotation.

.. code-block:: java

  @BatchInsert(sqlLog = SqlLogType.RAW)
  int[] insert(List<Employee> employees);

``SqlLogType.RAW`` outputs the SQL statement with its binding parameters in the log.
