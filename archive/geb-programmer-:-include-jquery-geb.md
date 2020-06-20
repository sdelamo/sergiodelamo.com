title: Geb Programmer : Include Jquery Geb
date: May 21, 2020
status: draft
---

# [%title]

[%date] #geb

```groovy
static void includeJquery(Browser browser) {
def library = 'https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js'
def createScript = "document.createElement('script')"
def st = "if (typeof(document.getElementById('jqueryscript')) == 'undefined' || document.getElementById('jqueryscript') == null) { document.body.appendChild(${createScript}).id='jqueryscript';document.getElementById('jqueryscript').src ='${library}'; }"
browser.js.exec(st)
}
```