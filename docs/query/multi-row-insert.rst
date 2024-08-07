==================
Multi-row insert
==================

.. contents::
   :depth: 3

Annotate the DAO method with ``@MultiInsert`` to execute a multi-row insert.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @MultiInsert
      int insert(List<Employee> employees);

      @MultiInsert
      MultiResult<ImmutableEmployee> insert(List<ImmutableEmployee> employees);
  }

By using multi-row insert, you can issue SQL statements such as the following:

.. code-block:: sql

    insert into EMPLOYEE (EMPLOYEE_ID, EMPLOYEE_NO, EMPLOYEE_NAME, AGE, VERSION)
    values (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)

The ``preInsert`` method of entity listener is called each entity when before executing insert if the entity listener is specified at :doc:`../entity` parameter.
Also the ``postInsert`` method of entity listener method is called each entity when after executing insert.

.. note::

  The databases that support this feature are:

  * H2
  * MySQL
  * PostgreSQL
  * SQL Server
  * Oracle Database

  However, in the case of SQL Server and Oracle, this feature cannot be executed on tables with an auto-increment primary key.

Return type
===========

If the type argument of the ``Iterable`` parameter is an immutable entity class, the return type must be ``org.seasar.doma.jdbc.MultiResult`` with that entity class as an element.

If the type argument of the ``Iterable`` parameter is a mutable entity class, the return type must be ``int`` that represents updated count.

Parameter type
==============

The parameter type must be a subtype of ``java.lang.Iterable`` that has the entity class as its element.

The parameter must not be ``null``.

Automatically generated values
==============================

During the execution of a multi-insert, automatically generated values will be set to the entity properties.

Identifier
----------

See :ref:`identity-auto-generation`.

Version number
--------------

If the application does not set a value to the version property or sets a value less than ``0``, the value ``1`` will be ultimately set to that property.

If the application explicitly sets a value greater than ``0`` to the version property, automatic generation will not occur.

See also :ref:`entity-version`.

Properties of @MultiInsert
==========================

exclude
-------

Entity properties specified in the ``exclude`` property of ``@MultiInsert`` will be excluded from the insert targets, even if they are set as ``insertable`` in the 
``@Column`` annotation.

.. code-block:: java

  @MultiInsert(exclude = {"name", "salary"})
  int insert(List<Employee> employees);

include
-------

Only the entity properties specified in the ``include`` property of ``@MultiInsert`` will be included in the insert targets.

If the same entity property is specified in both the ``exclude`` and ``include`` properties, that entity property will not be included in the insert targets.

Entity properties with ``insertable`` set to ``false`` in the ``@Column`` annotation will not be included in the insert targets, even if they are specified in the ``include`` property.

.. code-block:: java

  @MultiInsert(include = {"name", "salary"})
  int insert(List<Employee> employees);

duplicateKeyType
----------------

This property defines the strategy for handling duplicate keys during an insert operation.

It can take one of three values:

* ``DuplicateKeyType.UPDATE``: If a duplicate key is encountered, the existing row in the table will be updated.
* ``DuplicateKeyType.IGNORE``: If a duplicate key is encountered, the insert operation will be ignored, and no changes will be made to the table.
* ``DuplicateKeyType.EXCEPTION``: If a duplicate key is encountered, an exception will be thrown.

.. code-block:: java

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE)
  int insert(List<Employee> employees);

duplicateKeys
----------------

This property represents the keys that should be used to determine if a duplicate key exists. If the duplicate key exists, the operation will use the ``duplicateKeyType`` strategy to handle the duplicate key.

.. code-block:: java

  @MultiInsert(duplicateKeyType = DuplicateKeyType.UPDATE, duplicateKeys = {"employeeNo"})
  int insert(List<Employee> employees);

.. note::

  This property is only utilized when the ``duplicateKeyType`` strategy is either ``DuplicateKeyType.UPDATE`` or ``DuplicateKeyType.IGNORE``.

.. note::

  The MySQL dialect does not utilize this property.

Unique constraint violation
============================

``org.seasar.doma.jdbc.UniqueConstraintException`` is thrown if unique constraint violation is occurred.

Query timeout
==================

You can specify seconds of query timeout to ``queryTimeout`` property within ``@MultiInsert`` annotation.

.. code-block:: java

  @MultiInsert(queryTimeout = 10)
  int insert(List<Employee> employees);

Query timeout that is specified in config class is used if ``queryTimeout`` property is not set value.

SQL log output format
=====================

You can specify SQL log output format to ``sqlLog`` property within ``@MultiInsert`` annotation.

.. code-block:: java

  @MultiInsert(sqlLog = SqlLogType.RAW)
  int insert(List<Employee> employees);

``SqlLogType.RAW`` represent outputting log that is sql with a binding parameter.
