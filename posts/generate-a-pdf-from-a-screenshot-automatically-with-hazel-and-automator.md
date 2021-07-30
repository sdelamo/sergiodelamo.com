---
title: Automatically generate a PDF from a Screenshot 
summary: Combine Hazel and Automator to do it. 
banner_image: (optional) is the URL of an image to use as a banner. 
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-07-30T10:17:09+01:00
date_modified: 2021-07-30T10:17:09+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #hazel #macos #automator #automation

It is easy to [take a screenshot on your Mac](https://support.apple.com/en-us/HT201361). Everyday, I use `Shift + Command + 4`.

I often want to generate a PDF from the Screenshot. For example, it is the screenshot of an invoice which I want to file as PDF.

With [Automator](https://support.apple.com/guide/automator/welcome/mac) you can create a _Quick Action_ to help you do that: 

> Quick Actions are workflows that may be added to Finder, Touch Bar and the Services menu. You can manage Quick Actions in System Preferences.

![Automator Quick Action](https://images.sergiodelamo.com/automator-quickaction.png)

Automator ships with a built-in action to generate a PDF from an image: 

![](https://images.sergiodelamo.com/automator-new-pdf-from-images.png)

MacOS saves screenshots by default to the Desktop. Their name starts with _Screenshot_.

> By default, screenshots save to your desktop with the name ”Screen Shot [date] at [time].png.”

 You end up with something like `Screenshot 2021-07-29 at 13.20.29.png`.

Next, I use [Hazel](https://www.noodlesoft.com) to monitor by _Desktop_ folder. 

> Hazel watches whatever folders you tell it to, automatically organizing your files according to the rules you create

When I take a Screenshot, the Screenshot app saves a new image to the Desktop. The Hazel's rule checks if the new Desktop file is image and whether its name starts with _Screenshot_. If it does, it invokes the Automator Quick Action to generate a PDF. 

![Run an Automator Workflow from Hazel](https://images.sergiodelamo.com/hazel-run-automator-workflow.png)

To finish, it displays a notification as the icing on the cake.

