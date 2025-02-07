===============
Select
===============

.. contents::
   :depth: 3

To execute a search using the SELECT statement, annotate the DAO method with ``@Select``.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName);
      ...
  }

The ``@Select`` annotation requires an :doc:`SQL template <../sql>`. 
Describe the SQL template in an SQL file or in the ``@Sql`` annotation.

Search condition
=================

The search condition make use of method parameters.
The available types of parameters are as follows:

* :doc:`../basic`
* :doc:`../domain`
* Arbitrary type
* java.util.Optional containing either :doc:`../basic`, :doc:`../domain`, or arbitrary type as its element.
* java.util.Iterable containing either :doc:`../basic` or :doc:`../domain` as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

If the parameter type is one of either :doc:`../basic` or :doc:`../domain`, it is permissible to set the argument to ``null``. 
If the parameter type is anything other than these, the argument must not be ``null``.

Query using basic classes or domain classes
----------------------------------------------

Declare :doc:`../basic` or :doc:`../domain` as method parameters.

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

Use the :ref:`bind-variable-directive` to bind method parameters to SQL.

.. code-block:: sql

  select * from employee where employee_name = /* name */'hoge' and salary > /* salary */100

Query using arbitrary type
----------------------------------

When using arbitrary types as method parameters, use a dot ``.`` within the bind variable directive 
to perform field access or method invocation, and bind the result to SQL.

.. code-block:: java

  @Select
  List<Employee> selectByExample(Employee employee);

.. code-block:: sql

  select * from employee where employee_name = /* employee.name */'hoge' and salary > /* employee.getSalary() */100

Multiple parameters can be specified.

.. code-block:: java

  @Select
  List<Employee> selectByEmployeeAndDepartment(Employee employee, Department department);

Mapping to the IN clause
-----------------------------------------

To bind to the IN clause, use a subtype of ``java.lang.Iterable`` as the parameter.

.. code-block:: java

  @Select
  List<Employee> selectByNames(List<String> names);

.. code-block:: sql

  select * from employee where employee_name in /* names */('aaa','bbb','ccc')

Single record search
=====================

For single record searches, the return type of the method must be one of the following:

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* java.util.Optional containing either :doc:`../basic`, :doc:`../domain`, :doc:`../entity`, or java.util.Map<String, Object> as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Employee selectByNameAndSalary(String name, BigDecimal salary);

If the return type is not ``Optional`` and the result count is 0, ``null`` is returned.

If there are 2 or more search results, a ``NonUniqueResultException`` is thrown.

Multiple record search
========================

When searching for multiple records, specify ``java.util.List`` as the return type of the method. 
The elements of the ``List`` can be of the following types:

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* java.util.Optional containing either :doc:`../basic` or :doc:`../domain` as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  List<Employee> selectByNameAndSalary(String name, Salary salary);

If there are no search results, an empty list is returned.

.. _stream-search:

Stream search
==============

For processing a large number of records incrementally, stream search using ``java.util.stream.Stream`` can be utilized.

There are two types of stream searches: one method involves passing a Stream to ``java.util.Function``, 
and the other method involves returning a ``Stream`` as the return value.

Passing a Stream to Function
-----------------------------

Set the ``strategy`` property in the ``@Select`` annotation to ``SelectType.STREAM``, 
and add a subtype of ``java.util.Function<Stream<TARGET, RESULT>>`` as a method parameter.

.. code-block:: java

  @Select(strategy = SelectType.STREAM)
  BigDecimal selectByNameAndSalary(String name, BigDecimal salary, Function<Stream<Employee>, BigDecimal> mapper);

The caller of the DAO method passes a lambda expression that receives a stream and returns the result.

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  BigDecimal result = dao.selectByNameAndSalary(name, salary, stream -> {
      return ...;
  });

The type parameter ``TARGET`` of ``Function<Stream<TARGET>, RESULT>`` must be one of the following:

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* Either :doc:`../basic` or :doc:`../domain` is within java.util.Optional
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

Type parameter ``RESULT`` must match to Dao method return value.

Returning a Stream
---------------------------

You define ``java.util.stream.Stream`` to method return value.
You can use following type at property within ``Stream``.

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* java.util.Optional containing either :doc:`../basic` or :doc:`../domain` as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Select
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

The caller of the DAO method will be as follows:

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  try (Stream<Employee> stream = dao.selectByNameAndSalary(name, salary)) {
    ...
  }

.. warning::

  To ensure the proper closing of resources such as 
  ``java.sql.ResultSet``, ``java.sql.PreparedStatement``, and ``java.sql.Connection``, 
  always close the ``Stream``.

.. note::

  Due to the risk of forgetting to release resources when returning values, Doma displays a warning message. 
  To suppress the warning message, please specify ``@Suppress`` as follows:

.. code-block:: java

  @Select
  @Suppress(messages = { Message.DOMA4274 })
  Stream<Employee> selectByNameAndSalary(String name, BigDecimal salary);

.. _collector-search:

Collector search
================

Search results can be processed using ``java.util.Collector``.

To process search results using ``Collector``, set the ``strategy`` element of ``@Select`` to ``SelectType.COLLECT``, 
and define a subtype of ``java.stream.Collector<TARGET, ACCUMULATION, RESULT>`` or 
``java.stream.Collector<TARGET, ?, RESULT>`` as a method parameter.

.. code-block:: java

  @Select(strategy = SelectType.COLLECT)
  <RESULT> RESULT selectBySalary(BigDecimal salary, Collector<Employee, ?, RESULT> collector);

The caller of the DAO method passes an instance of ``Collector``.

.. code-block:: java

  EmployeeDao dao = new EmployeeDaoImpl();
  Map<Integer, List<Employee>> result =
      dao.selectBySalary(salary, Collectors.groupingBy(Employee::getDepartmentId));

The type parameter ``TARGET`` of ``Collector<TARGET, ACCUMULATION, RESULT>`` must be one of the following:

* :doc:`../basic`
* :doc:`../domain`
* :doc:`../entity`
* java.util.Map<String, Object>
* java.util.Optional containing either :doc:`../basic` or :doc:`../domain` as its element.
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

The type parameter ``RESULT`` of ``Collector<TARGET, ACCUMULATION, RESULT>`` must match the return type of the DAO method.

.. note::

  Collect search is the shortcut that pass to Function within stream search.
  You can do equivalent by using `collect`` method in ``Stream`` object that is getting from stream search.

Aggregate strategy
==================

The ``aggregateStrategy`` element in ``@Select`` allows query results to be mapped
to hierarchical entity structures based on a predefined aggregate strategy.

.. code-block:: java

  @Select(aggregateStrategy = EmployeeStrategy.class)
  Employee selectByName(String name);

For more details, see :doc:`../aggregate-strategy`.

.. note::

  The aggregate strategy cannot be used in combination with :ref:`stream-search` or :ref:`collector-search`.

Search options
============================

By using ``SelectOptions``, you can convert the SELECT statement into SQL for paging or pessimistic locking purposes.

``SelectOptions`` is defined as a parameter of the DAO method.

.. code-block:: java

  @Dao
  public interface EmployeeDao {
      @Select
      List<Employee> selectByDepartmentName(String departmentName, SelectOptions options);
      ...
  }

You can obtain an instance of ``SelectOptions`` through a static ``get`` method.

.. code-block:: java

  SelectOptions options = SelectOptions.get();

Paging
----------

To implement paging, specify the starting position with the ``offset`` method and 
the number of records to retrieve with the ``limit`` method in ``SelectOptions``. 
Then, pass an instance of ``SelectOptions`` to the DAO method.

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10);
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

Paging is achieved by modifying the original SQL, which must meet the following conditions: 

* it is a SELECT statement.
* it does not perform set operations like UNION, EXCEPT, or INTERSECT at the top level (though subqueries are allowed).
* it does not include paging operations.

Additionally, specific conditions must be met according to the dialect.

+------------------+-------------------------------------------------------------------------------------+
| Dialect          |    Condition                                                                        |
+==================+=====================================================================================+
| Db2Dialect       |    When specifying an offset, all columns listed in the ORDER BY clause             |
|                  |    must be included in the SELECT clause.                                           |
+------------------+-------------------------------------------------------------------------------------+
| Mssql2008Dialect |    When specifying an offset, all columns listed in the ORDER BY clause             |
|                  |    must be included in the SELECT clause.                                           |
+------------------+-------------------------------------------------------------------------------------+
| MssqlDialect     |    When specifying an offset, the ORDER BY clause is required.                      |
+------------------+-------------------------------------------------------------------------------------+
| StandardDialect  |    The ORDER BY clause is required.                                                 |
|                  |    All columns listed in the ORDER BY clause must be included in the SELECT clause. |
+------------------+-------------------------------------------------------------------------------------+

Pessimistic concurrency control
---------------------------------

You can indicate pessimistic concurrency control using the ``forUpdate`` method of ``SelectOptions``.

.. code-block:: java

  SelectOptions options = SelectOptions.get().forUpdate();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);

``SelectOptions`` provides methods for pessimistic concurrency control with names starting with `forUpdate`, 
such as ``forUpdate`` to specify aliases for tables or columns to be locked, 
and ``forUpdateNowait`` to acquire locks without waiting.

Pessimistic concurrency control is achieved by rewriting the original SQL, which must meet the following conditions:

* it is a SELECT statement.
* it does not perform set operations like UNION, EXCEPT, or INTERSECT at the top level (though subqueries are allowed).
* it does not include pessimistic concurrency control operations.


Depending on the dialect, some or all of the methods for pessimistic concurrency control may not be available for use.

+------------------+-----------------------------------------------------------------------------+
| Dialect          |    Description                                                              |
+==================+=============================================================================+
| Db2Dialect       |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| H2Dialect        |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| HsqldbDialect    |    You can use forUpdate().                                                 |
+------------------+-----------------------------------------------------------------------------+
| Mssql2008Dialect |    You can use forUpdate() and forUpdateNowait().                           |
|                  |    However, FROM clauses in original SQL must consist single table.         |
+------------------+-----------------------------------------------------------------------------+
| MysqlDialect     |    You can use forUpdate()                                                  |
+------------------+-----------------------------------------------------------------------------+
| MysqlDialect (V8)|    You can use forUpdate(), forUpdate(String... aliases),                   |
|                  |    forUpdateNowait(), and forUpdateNowait(String... aliases).               |
+------------------+-----------------------------------------------------------------------------+
| OracleDialect    |    You can use forUpdate(), forUpdate(String... aliases),                   |
|                  |    forUpdateNowait(), forUpdateNowait(String... aliases),                   |
|                  |    forUpdateWait(int waitSeconds), and                                      |
|                  |    forUpdateWait(int waitSeconds, String... aliases).                       |
+------------------+-----------------------------------------------------------------------------+
| PostgresDialect  |    You can use forUpdate() and forUpdate(String... aliases).                |
+------------------+-----------------------------------------------------------------------------+
| StandardDialect  |    You can not use all of pessimistic concurrency control method.           |
+------------------+-----------------------------------------------------------------------------+

Count
---------

By calling the ``count`` method of ``SelectOptions``, you can retrieve the total count of records. 
Typically, this is used in combination with paging options to retrieve the total count of records 
when not filtering through paging.

.. code-block:: java

  SelectOptions options = SelectOptions.get().offset(5).limit(10).count();
  EmployeeDao dao = new EmployeeDaoImpl();
  List<Employee> list = dao.selectByDepartmentName("ACCOUNT", options);
  long count = options.getCount();

The total count of records is obtained using the ``getCount`` method of ``SelectOptions`` after calling the DAO method. 
If the ``count`` method hasn't been executed before the DAO method call, the ``getCount`` method will return -1.

Ensure the existence of search results
=======================================

If you want to ensure that there is at least one search result, specify ``true`` for the ``ensureResult`` element of ``@Select``.

.. code-block:: java

  @Select(ensureResult = true)
  Employee selectById(Integer id);

If there are no search results, a ``NoResultException`` will be thrown.

Ensure the mapping of search results
====================================

If you want to ensure that all columns of the result set are mapped to properties of the entity without missing any, 
specify ``true`` for the ``ensureResultMapping`` element of ``@Select``.

.. code-block:: java

  @Select(ensureResultMapping = true)
  Employee selectById(Integer id);

If there are properties in the entity that are not mapped to columns in the result set, 
a ``ResultMappingException`` will be thrown.

Query timeout
==================

You can specify the query timeout in seconds for the ``queryTimeout`` property within the ``@Select`` annotation.

.. code-block:: java

  @Select(queryTimeout = 10)
  List<Employee> selectAll();


If the value of the ``queryTimeout`` property is not set, the query timeout specified in the :doc:`../config` will be used.

Fetch size
==============

You can specify the fetch size in the ``fetchSize`` property within the ``@Select`` annotation.

.. code-block:: java

  @Select(fetchSize = 20)
  List<Employee> selectAll();

If the value of the ``fetchSize`` property is not set, the fetch size specified in the :doc:`../config` will be used.

Max row count
===============

You can specify the maximum number of rows in the ``maxRows`` property within the ``@Select`` annotation.

.. code-block:: java

  @Select(maxRows = 100)
  List<Employee> selectAll();

If the value of the ``maxRows`` property is not set, the maximum number of rows specified in the :doc:`../config` will be used.

The naming convention for the keys of the Map
=============================================

If you are mapping search results to ``java.util.Map<String, Object>``, 
you can specify the naming convention for the keys of the map in the ``mapKeyNaming`` element of ``@Select``.

.. code-block:: java

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> selectAll();

``MapKeyNamingType.CAMEL_CASE`` indicates that the column names will be converted to camel case. 
There are also conventions to convert column names to uppercase or lowercase.

The final conversion result is determined by the value specified here and the implementation of ``MapKeyNaming``
specified in the :doc:`../config`.

Output format of SQL logs
=========================

You can specify the format of SQL log output in the ``sqlLog`` element of the ``@Select`` annotation.

.. code-block:: java

  @Select(sqlLog = SqlLogType.RAW)
  List<Employee> selectById(Integer id);

``SqlLogType.RAW`` indicates logging SQL with bound parameters.
