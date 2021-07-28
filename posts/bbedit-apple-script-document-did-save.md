---
title: Run a Gradle build when you save in BBEdit
summary: Use BBEdit's Attachment Scripts and AppleScript
banner_image: https://images.sergiodelamo.com/bbedit-attachment-scripts.png
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-07-28T10:47:51+01:00
date_modified: 2021-07-28T10:47:51+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #automation #bbedit #gradle #applescript

My blog is a static website which I generate with a [Gradle](http://gradle.org) build. Gradle builds the website if I run:

`cd ~/github/sdelamo/sergiodelamo.com/;./gradlew build`

My posts are Markdown files under `~/github/sdelamo/sergiodelamo.com/posts`

I want [BBEdit](https://www.barebones.com/products/bbedit/) to run my Gradle build when I save a Markdown file in my blog's folder. Thanks to BBEdit's AppleScript integration it is possible. 

I created an AppleScript in the _Attachment Scripts_ folder:

> The Attachment Scripts folder contains AppleScripts which are run at specific points: when BBEdit starts or quits; and when documents are open, saved, and closed.

I saved the following _AppleScript_ script at `~/Library/Application Support/BBEdit/Attachment Scripts/Document.documentDidSave.scpt`. 

It runs when I save a blog post's Markdown file:

```applescript
on documentDidSave(doc)
    set p to file of doc
    if (p as text) ends with ".md" and (p as text) contains "github:sdelamo:sergiodelamo.com" then
        set buildScript to "cd ~/github/sdelamo/sergiodelamo.com/;./gradlew build"
        tell application "Terminal"
            if (exists window 1) and not busy of window 1 then
                do script buildScript in window 1
            else
                do script buildScript
            end if
        end tell
    end if
end documentDidSave
```

This script allows me to focus on writing. 