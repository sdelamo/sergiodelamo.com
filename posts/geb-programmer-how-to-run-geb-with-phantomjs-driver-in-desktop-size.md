---
title: Geb Programmer : How to run Geb with PhantomJS Driver in desktop size
date_published: 2016-06-17T07:07:00+01:00
date_modified: 2016-06-17T07:07:00+01:00
---

# [%title]

[%date_published]

Tags: #geb

If you are developing your Geb programms with Firefox or Chrome driver, they will probably be running in a traditional desktop resolution. You may be puzzled when your tests fail in PhantomJS driver.

## Why?

After further investigation you will probably realise that the tests are failing because the code you developed will only work when you run the test in desktop resolution. The website you are working against may be using fancy adaptive design tricks with hidden content which may become visible in mobile resolutions and viceversa.

## Maximize!

If you execute Geb with PhantomJS driver, by default, it will run in 400x300. You may want to run it in a desktop resolution. Don't dispare. It is easy. You can tell Geb driver to maximize its window.

```groovy

import geb.Browser
..
.
.
def browser = new Browser(driver: new PhantomJSDriver())
println browser.driver.manage().window().size.height // 300
println browser.driver.manage().window().size.width // 400
browser.driver.manage().window().maximize()
println browser.driver.manage().window().size.height // 768
println browser.driver.manage().window().size.width // 1366
```
