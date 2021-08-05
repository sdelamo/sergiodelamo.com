---
title: TextExpander Popup Menu
banner_image: https://images.sergiodelamo.com/textexpander-popup-menu.png
summary: Text expansion macros can have a UI to guide you
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-05T10:41:50+01:00
date_modified: 2021-08-05T10:41:50+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #automation #textexpander

We produce HTML pages for each [Micronaut Guide](https://guides.micronaut.io). However, we write them as plain text files (more precisely Asciidoc files). We have custom macros to include common snippets. For example, to generate a callout about an `@Introspected` annotation we write: 

`callout:introspected[1]`

Automation is not just about saving time. It is a about correctness. I have created a [Text Expander](https://textexpander.com/) snippet to help me write those callouts.  

I trigger the snippet with `zcallout`

![](https://images.sergiodelamo.com/text-expander-callout.png)

and it uses a Popup Menu: 

![](https://images.sergiodelamo.com/text-expander-popup-menu.png)

this is how it works: 

![](https://images.sergiodelamo.com/CalloutMacro.gif)



