==================
Embeddable classes
==================

.. contents::
   :depth: 3

Embeddable classes group properties for entity classes.

Embeddable definition
=====================

Here is an example of how to define an embeddable class:

.. code-block:: java

  @Embeddable
  public class Address {

      final String city;

      final String street;

      @Column(name = "ZIP_CODE")
      final String zip;

      public Address(String city, String street, String zip) {
          this.city = city;
          this.street = street;
          this.zip = zip;
      }
  }

You can apply the ``@Embeddable`` annotation to both classes and records:

.. code-block:: java

  @Embeddable
  public record Address(
    String city,
    String street,
    @Column(name = "ZIP_CODE")String zip) {
  }

The embeddable class is used as the entity field type:

.. code-block:: java

  @Entity
  public class Employee {
      @Id
      Integer id;

      Address address;
  }

The above entity definition is equivalent to the following one:

.. code-block:: java

  @Entity
  public class Employee {
      @Id
      Integer id;

      String city;

      String street;

      @Column(name = "ZIP_CODE")
      String zip;
  }

Naming convention
-----------------

The naming convention is inherited from the enclosing entity class.

Field definition
================

By default, all fields are persistent and correspond to database columns or result set columns.

The field type must be one of the following:

* :doc:`basic`
* :doc:`domain`
* java.util.Optional, whose element is either :doc:`basic` or :doc:`domain`
* java.util.OptionalInt
* java.util.OptionalLong
* java.util.OptionalDouble

.. code-block:: java

  @Embeddable
  public class Address {
      ...
      String street;
  }

Column
------

You can specify the corresponding column name with the ``@Column`` annotation:

.. code-block:: java

  @Column(name = "ZIP_CODE")
  final String zip;

Transient
---------

If an embeddable has fields that you don’t want to persist, you can annotate them using ``@Transient``:

Method definition
=================

There are no limitations in the use of methods.

Example
=======

.. code-block:: java

  Employee employee = new Employee(); // Entity
  Address address = new Address("Tokyo", "Yaesu", "103-0028"); // Embeddable
  employee.setAddress(address);
