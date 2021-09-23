---
title: IntelliJ IDEA - Disable wildcard imports to always import single classes
external_url: https://www.jetbrains.com/help/idea/creating-and-optimizing-imports.html#disable-wildcard-imports
summary: Go to Editor > Code Style > Java > Imports
date_published: 2021-07-20T09:51:47+01:00
---

# [%title]

keywords:idea


[IntelliJ IDEA Documentation]([%external_url]):

> In the Settings/Preferences dialog ⌘,, select Editor | Code Style | Java | Imports.
> - Make sure that the Use single class import option is enabled.
> - In the Class count to use import with '*' and Names count to use static import with '*' fields, specify values that definitely exceed the number of classes in a package and the number of names in a class (for example, 999 ).

It is possible to change a wildcard import: 

> To replace an import statement with single class imports in a file without changing the settings, place the caret at the import statement, press ⌥⏎ (or use the intention action the Intention action icon icon), and select Replace with single class imports.

