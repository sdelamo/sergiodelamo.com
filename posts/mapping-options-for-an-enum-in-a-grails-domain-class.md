---
title: Mapping options for an Enum in a Grails Domain class
author.name: Sergio del Amo
author.url: https://sergiodelamo.com
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2017-09-18T11:39:00+01:00
date_modified: 2017-09-18T11:39:00+01:00
---

# [%title]

[%author.name] [%date_published]

#grails

The next example illustrates the different options to map an enum.

Given the next code:

```groovy
package demo

import groovy.transform.CompileStatic

@CompileStatic
class BootStrap {

    def init = { servletContext -&amp;gt;
        Book.saveAll(
            new Book(name: 'Grails 3 - Step by Step',
                     status: Status.NOT_SET),
            new Book(name: 'Grails 3 - A practical guide to application development',
                     status: Status.BORROWED),
            new Book(name: 'Falando de Grails',
                     status: Status.SOLD),
        )
    }
    def destroy = { }
}
```

```groovy
package demo

import groovy.transform.CompileStatic

@CompileStatic
enum Status {
    NOT_SET(-1),
    BORROWED(0),
    SOLD(1),

    final int id
    private Status(int id) { this.id = id }
}
```

For enumType `ordinal`

```groovy
package demo

class Book {
    String name
    Status status

    static mapping = {
        status enumType: 'ordinal'
    }
}
```

The enum will be mapped in the database as:

ID	VERSION	NAME	STATUS
1	0	Grails 3 - Step by Step	0
2	0	Grails 3 - A practical guide to application development	1
3	0	Falando de Grails	2

For enumType `identity`

```groovy
package demo

class Book {
    String name
    Status status

    static mapping = {
        status enumType: 'identity'
    }
}
```

The enum will be mapped in the database as:

ID	VERSION	NAME	STATUS
1	0	Grails 3 - Step by Step	-1
2	0	Grails 3 - A practical guide to application development	0
3	0	Falando de Grails	1
For enumType `string`

```groovy
package demo

class Book {
    String name
    Status status

    static mapping = {
        status enumType: 'string'
    }
}
```

The enum will be mapped in the database as:

ID	VERSION	NAME	STATUS
1	0	Grails 3 - Step by Step	NOT_SET
2	0	Grails 3 - A practical guide to application development	BORROWED
3	0	Falando de Grails	SOLD

