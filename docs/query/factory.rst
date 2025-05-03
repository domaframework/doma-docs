=========
Factories
=========

.. contents::
   :depth: 3

To obtain instances from the factory methods of ``java.sql.Connection``,
annotate DAO methods with one of the following annotations:

* @ArrayFactory
* @BlobFactory
* @ClobFactory
* @NClobFactory
* @SQLXMLFactory

Creating Array instances
========================

The return type must be ``java.sql.Array`` and the number of parameters must be one.
The parameter type must be an array type and the parameter must not be null.

Specify a database type name in the ``typeName`` element of the ``@ArrayFactory`` annotation:

.. code-block:: java

  @ArrayFactory(typeName = "integer")
  Array createIntegerArray(Integer[] elements);

Creating Blob instances
=======================

The return type must be ``java.sql.Blob`` and the number of parameters must be zero:

.. code-block:: java

  @BlobFactory
  Blob createBlob();

Creating Clob instances
=======================

The return type must be ``java.sql.Clob`` and the number of parameters must be zero:

.. code-block:: java

  @ClobFactory
  Clob createClob();

Creating NClob instances
========================

The return type must be ``java.sql.NClob`` and the number of parameters must be zero:

.. code-block:: java

  @NClobFactory
  NClob createNClob();

Creating SQLXML instances
=========================

The return type must be ``java.sql.SQLXML`` and the number of parameters must be zero:

.. code-block:: java

  @SQLXMLFactory
  SQLXML createSQLXML();
