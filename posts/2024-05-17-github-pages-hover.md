---
title: Hosting a static website in GitHub Pages and Hover
summary: In this blog post, I show you how to host a website in [GitHub Pages](https://pages.github.com) and manage the domain with [Hover](https://www.hover.com)
date_published: 2024-05-17T10:12:17+01:00
  keywords:github,github-actions,ghpages,hover,static-website,boostrap
---

# [%title]

[%summary]

## Website

I recently joined a local chess association in Guadalajara. They did not have a website, so I offered to create one for them. [The website](https://ajedrezcallejeroguadalajara.com) is a static website with HTML and [Bootstrap](https://getbootstrap.com).

The website is simple, but it conveys the association information, and [it scores 100% in performance, accessibility, best practices and SEO](https://pagespeed.web.dev/analysis/https-ajedrezcallejeroguadalajara-com/uamhg91xi4?form_factor=desktop).

## GitHub Organization

Instead of creating it from my personal account, I created a [GitHub Organization](https://github.com/ajedrezcallejeroguadalajara/) dedicated to the association.
I [verified the domain at the GitHub Organization level](https://docs.github.com/en/organizations/managing-organization-settings/verifying-or-approving-a-domain-for-your-organization).
**You need to verify the domain at the organizational level, not the repository level**.

## GitHub Pages

[Micronaut](https://docs.micronaut.io) publishes its documentation to GitHub Pages deploying from branch `gh-pages`. For this website, I used _GitHub Actions as the source_ instead. The website changes seem remarkably faster.

### GitHub Action Workflow

The following GitHub Action workflow deploys the contents of the `dist` folder.

```yaml
name: Deploy static content to Pages
on:
  push:
    branches: ["main"]
permissions:
  contents: read
  pages: write
  id-token: write
concurrency:
  group: "pages"
  cancel-in-progress: false
jobs:
  deploy:
    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
      - name: Setup Pages
        uses: actions/configure-pages@v5
      - name: Upload artifact
        uses: actions/upload-pages-artifact@v3
        with:
          path: 'dist'
      - name: Deploy to GitHub Pages
        id: deployment
        uses: actions/deploy-pages@v4
```

## Domain

I use [Hover](https://www.hover.com) as a domain registrar. I use the following [DNS configuration as pointed out by GitHub Pages documentation](https://docs.github.com/en/pages/getting-started-with-github-pages/securing-your-github-pages-site-with-https#verifying-the-dns-configuration).

| TYPE | HOST                                                |        VALUE        |         TTL | 
|:-----|:----------------------------------------------------|:-------------------:|------------:|
| A    | @                                                   |   185.199.109.153   |  15 Minutes |
| A    | @                                                   |   185.199.110.153   |  15 Minutes |
| A    | @                                                   |   185.199.111.153   |  15 Minutes |
| A    | @                                                   |   185.199.108.153   |  15 Minutes |
| AAAA | @                                                   | 2606:50c0:8000::153 |  15 Minutes |
| AAAA | @                                                   | 2606:50c0:8001::153 |  15 Minutes |
| AAAA | @                                                   | 2606:50c0:8002::153 |  15 Minutes |
| AAAA | @                                                   | 2606:50c0:8003::153 |  15 Minutes | 
| TXT  | _github-pages-challenge-ajedrezcallejeroguadalajara |           89676ddaaeb7a6adec23d364020599 |  15 Minutes | 
| CNAME | www | ajedrezcallejeroguadalajara.github.io | 15 Minutes  | 


## Cost
The total cost for the association is 18 USD per year - the cost of the domain.

## Summary
I hope this post shows you how easy is to host a website in [GitHub Pages](https://pages.github.com) and manage the domain with [Hover](https://www.hover.com). 

If you are in the Guadalajara (Spain) area, I hope to play chess against you soon. You can find the meetup information at [ajedrezcallejeroguadalajara.com](https://ajedrezcallejeroguadalajara.com).

