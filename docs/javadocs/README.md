# JavaDocs

This folder documents the JavaDocs publishing plan. It is not the generated JavaDocs output.

## Generate JavaDocs

Run:

```bash
mvn javadoc:aggregate
```

The aggregate API reference is generated at:

```text
target/site/apidocs/
```

## Separate From MkDocs

MkDocs source lives in:

```text
docs/mkdocs/
```

Generated JavaDocs should not be committed into `docs/mkdocs/`.

The Markdown documentation is the human-friendly guide. JavaDocs are the API reference for classes, methods, parameters, and return types.

## Suggested Publishing Layout

For GitHub Pages or another static host:

```text
/YALP/      -> MkDocs site
/YALP/jd/   -> JavaDocs API reference
```

A release workflow can later copy `target/site/apidocs/` into a published `jd/` folder. Keep that workflow optional so normal contributors only need Maven and MkDocs locally.

