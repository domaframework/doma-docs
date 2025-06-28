# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the documentation repository for Doma, a database access framework for Java. The documentation is built using Sphinx and hosted on ReadTheDocs. The repository manages:

- English documentation (primary)
- Japanese translation
- Multiple output formats (HTML, PDF, ePub)

## Development Commands

### Setup
```bash
cd docs
pip install -r requirements.txt
```

### Local Development
```bash
# Start auto-rebuilding server for local development
cd docs
sphinx-autobuild . _build/html
# Visit http://127.0.0.1:8000
```

### Building Documentation
```bash
cd docs
# Build HTML
make html

# Build all formats
make
```

### Localization (for maintainers only)
```bash
cd docs
# Generate POT files for translation
sphinx-build -b gettext . _build/gettext
```

## Architecture

### File Structure
- `/docs/` - Main documentation source in reStructuredText (.rst) format
- `/docs/_static/` - Static assets (CSS, images, etc.)
- `/docs/locale/ja/LC_MESSAGES/` - Japanese translation files (.po)
- `/docs/conf.py` - Sphinx configuration with version variables and Jinja templating
- `/docs/requirements.txt` - Python dependencies for Sphinx

### Documentation Organization
- Documentation follows a hierarchical structure with `index.rst` as the master document
- Content is organized into logical sections: getting-started, config, basic, domain, entity, query/, etc.
- Each section can contain multiple .rst files with cross-references

### Version Management
Version numbers for Doma and related tools are centrally managed in `docs/conf.py` in the `html_context` dictionary:
- `doma_version`: Core Doma framework version
- `doma_compile_version`: Compile plugin version  
- `doma_codegen_version`: Code generation plugin version
- Additional plugin versions as needed

These variables can be used in documentation via Jinja templating.

### Translation Workflow
- English content is written in .rst files
- POT files for translation are generated using `sphinx-build -b gettext`
- Translated .po files are stored in `/docs/locale/ja/LC_MESSAGES/`

## Content Guidelines

- Use reStructuredText (.rst) format for all documentation
- Follow existing section hierarchy and cross-referencing patterns
- Include code examples with proper syntax highlighting
- Version-specific information should use the variables from conf.py
- Images should be placed in `/docs/images/` directory