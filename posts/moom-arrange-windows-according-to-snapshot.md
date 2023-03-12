---
title: Restore windows layouts with Moom
summary: Moom has some AppleScript functionality.
date_published: 2023-03-12T08:03:22+01:00
keywords:moon,applescript,automation,bunch
---

# [%title]

[Moom](https://manytricks.com/moom/) has capabilities to _Save and restore window layouts_.

> Set up a collection of windows in the size and locations you wish, then save the layout.  Restore the layout via an assigned hot key or via Moom's menus.

[%summary]

```AppleScript
tell application "Moom" to arrange windows according to snapshot "Snapshot"
```

Next is to [use Moom in MacOS Shortcuts](https://manytricks.com/blog/?p=5662) and with [Bunch](https://brettterpstra.com/2020/09/14/using-moom-with-bunch-for-window-management/).