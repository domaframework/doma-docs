=============================
Aggregate strategy interfaces
=============================

.. contents::
   :depth: 3

The aggregate strategy defines how to construct an entity aggregate from an arbitrary SELECT statement.
It provides a structured way to map relational query results into hierarchical entity structures by specifying
how entities should be linked together.

Aggregate strategy definition
=============================

An aggregate strategy is represented by annotating an interface with ``@AggregateStrategy``.
This annotation is used to define how an entity aggregate is reconstructed from a query result.

.. code-block:: java

    @AggregateStrategy(root = Dept.class, tableAlias = "d")
    interface DeptStrategy {
        ...
    }

- The ``root`` element specifies the entity class that serves as the root of the aggregate.
- The ``tableAlias`` element specifies the alias for the table corresponding to the root entity class.
  This alias must be used in the SELECT statement to correctly map query results to entity properties.

Annotation linker definition
=============================

An aggregate strategy must contain at least one field of type ``BiFunction`` annotated with ``@AssociationLinker``.
The ``BiFunction`` is responsible for dynamically associating two entity instances.

.. code-block:: java

    @AggregateStrategy(root = Dept.class, tableAlias = "d")
    interface DeptStrategy {
        @AssociationLinker(propertyPath = "empList", tableAlias = "e")
        BiFunction<Dept, Emp, Dept> dept = (d, e) -> {
            d.getEmpList().add(e);
            e.setDept(d);
            return d;
        };

        @AssociationLinker(propertyPath = "empList.address", tableAlias = "a")
        BiFunction<Emp, Addr, Emp> address = (e, a) -> {
            e.setAddress(a);
            return e;
        };
    }

- The first and third type parameters of ``BiFunction`` represent the class that owns the property,
  while the second type parameter represents the property type.
- The ``propertyPath`` element specifies the target property's name as a dot-separated path from the root entity class.
- The ``tableAlias`` element specifies the alias for the table corresponding to the entity class used as the second
  type parameter of ``BiFunction``. This alias must be used in the SELECT statement.

Example
================

The ``DeptStrategy`` described above is based on the following entity definitions:

.. code-block:: java

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    class Dept {
        @Id Integer deptId;
        String deptName;
        @Association List<Emp> empList = new ArrayList<>();

        // getter, setter
    }

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    class Emp {
        @Id Integer empId;
        String empName;
        Integer deptId;
        Integer addressId;
        @Association Dept dept;
        @Association Address address;

        // getter, setter
    }

    @Entity(naming = NamingType.SNAKE_LOWER_CASE)
    class Address {
        @Id Integer addressId;
        String street;

        // getter, setter
    }

In entity classes, association properties must be annotated with ``@Association``.
These properties can be linked using ``@AssociationLinker``.

Using an aggregate strategy
---------------------------

``DeptStrategy`` is used by specifying it in the ``aggregateStrategy`` element of ``@Select``:

.. code-block:: java

    @Dao
    interface DeptDao {
        @Select(aggregateStrategy = DeptStrategy.class)
        Dept selectById(Integer deptId);
    }

For the ``selectById`` method, the following SELECT statement is required:

.. code-block:: sql

    select
        d.dept_id as d_dept_id,
        d.dept_name as d_dept_name,
        e.emp_id as e_emp_id,
        e.emp_name as e_emp_name,
        e.dept_id as e_dept_id,
        e.address_id as e_address_id,
        a.address_id as a_address_id,
        a.street as a_street
    from
        dept d
    left outer join emp e
        on d.dept_id = e.dept_id
    left outer join address a
        on e.address_id = a.address_id
    where
        d.dept_id = /* deptId */0

### Column aliasing rules
- The table aliases must match those defined in ``DeptStrategy``.
- Column aliases must be prefixed with the table alias followed by an underscore (``_``).
  For example, ``d.dept_id`` is aliased as ``d_dept_id`` and ``e.emp_id`` as ``e_emp_id``.

Using the expansion directive
-----------------------------

By using the :ref:`expansion directive <expand>`, the above SELECT statement can be written more concisely:

.. code-block:: sql

    select
        /*%expand */*
    from
        dept d
    left outer join emp e
        on d.dept_id = e.dept_id
    left outer join address a
        on e.address_id = a.address_id
    where
        d.dept_id = /* deptId */0

How expansion works
~~~~~~~~~~~~~~~~~~~

- The ``/*%expand */*`` directive automatically expands into a column list using predefined aliasing rules.
- By default, all columns from all tables are included in the result set.

To selectively expand only specific tables, pass a comma-separated list of table aliases:

.. code-block:: sql

    select
        /*%expand "e, d" */*,
        a.address_id as a_address_id,
        a.street as a_street
    from
        dept d
    left outer join emp e
        on d.dept_id = e.dept_id
    left outer join address a
        on e.address_id = a.address_id
    where
        d.dept_id = /* deptId */0

- Here, only columns from tables ``e`` (``emp``) and ``d`` (``dept``) are expanded.
- The columns from table ``a`` (``address``) are explicitly specified.

