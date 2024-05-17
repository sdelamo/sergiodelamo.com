---
title: Deploy a MkDocs site to GitHub Pages via GitHub Actions
summary: This post shows a [GitHub Action](https://github.com/features/actions) workflow to build and deploy a [MkDocs](https://www.mkdocs.org) site to [GitHub Pages](https://pages.github.com). 
date_published: 2024-05-17T23:27:38+01:00
keywords:github-actions,mkdocs,github-pages
---

# [%title]

The following GitHub Action workflow to build and deploy the site to GitHub Pages `gh-pages` branch:

```yaml
name: Publish
on:
  push:
    branches: [ "main" ]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - name: Set up Python 3.10
      uses: actions/setup-python@v3
      with:
        python-version: "3.10"
    - name: ⚙️ Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install mkdocs
    - name: 🏗️ Build Site
      run: mkdocs build
    - name: 🚀 Deploy
      uses: JamesIves/github-pages-deploy-action@v4
      with:
        folder: site
```