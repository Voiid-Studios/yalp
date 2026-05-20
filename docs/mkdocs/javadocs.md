# JavaDocs

The Markdown documentation explains how to use YALP.
The JavaDocs provide the API reference for classes, methods, parameters, and return types.

The generated JavaDocs are separate from the MkDocs source. Do not place generated JavaDoc HTML inside `docs/mkdocs/`.

When published, JavaDocs should be available at:

```text
/jd/
```

Generate locally with:

```bash
mvn javadoc:aggregate
```

By default, Maven writes the aggregate JavaDocs to:

```text
target/site/apidocs/
```

Suggested GitHub Pages layout:

```text
/YALP/      -> MkDocs site
/YALP/jd/   -> JavaDocs API reference
```

The JavaDocs layout follows the standard modern JavaDoc site structure, with an `index.html`, package pages, class pages, search indexes, and resource folders.

