# Contributing guide

## Legal

All original contributions to doma-docs are licensed under
the ASL - [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0) or later.

## Reporting an issue

This project uses GitHub issues to manage the issues. Open an issue directly in GitHub.

Write the issue in English to share it with many people.

## Before you contribute

To contribute, use GitHub Pull Requests, from your own fork.

## Setup

We use [Sphinx](http://sphinx-doc.org) to generate documents.
The generated documents are hosted on the ReadTheDocs.

- https://doma.readthedocs.io/

To use Sphinx, you need Python.

#### Install Sphinx

Navigate to the docs directory and run the `pip install` command:

```
$ cd docs
$ pip install -r requirements.txt
```

#### Generate HTML files

Execute the `sphinx-autobuild` command in the docs directory:

```
$ sphinx-autobuild . _build/html
```

Visit the webpage served at http://127.0.0.1:8000.

Your environment is now set up.
Please verify that the modifications to the RST file are correctly converted into an HTML file, and then submit a pull request.

## Tasks to Be Performed by the Localization Manager

#### Generate POT files

Execute the `sphinx-build` command in the docs directory:

```
$ sphinx-build -b gettext . _build/gettext
```
