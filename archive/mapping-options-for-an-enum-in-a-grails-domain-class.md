title: Mapping options for an Enum in a Grails Domain class
date: May 21, 2020
---

# [%title]

[%date] #grails


The next example illustrates the different options to map an enum.

Given the next code:

```groovy
package demo

import groovy.transform.CompileStatic

@CompileStatic
class BootStrap {

def init = { servletContext -&gt;
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

<table>
<thead>
<tr>
<th> ID</th>
<th>VERSION</th>
<th>NAME</th>
<th>STATUS</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>0</td>
<td>Grails 3 - Step by Step</td>
<td>0</td>
</tr>
<tr>
<td>2</td>
<td>0</td>
<td>Grails 3 - A practical guide to application development</td>
<td>1</td>
</tr>
<tr>
<td>3</td>
<td>0</td>
<td>Falando de Grails</td>
<td>2</td>
</tr>
</tbody>
</table>

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

<table>
<thead>
<tr>
<th> ID</th>
<th>VERSION</th>
<th>NAME</th>
<th>STATUS</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>0</td>
<td>Grails 3 - Step by Step</td>
<td>-1</td>
</tr>
<tr>
<td>2</td>
<td>0</td>
<td>Grails 3 - A practical guide to application development</td>
<td>0</td>
</tr>
<tr>
<td>3</td>
<td>0</td>
<td>Falando de Grails</td>
<td>1</td>
</tr>
</tbody>
</table>

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

<table>
<thead>
<tr>
<th> ID</th>
<th>VERSION</th>
<th>NAME</th>
<th>STATUS</th>
</tr>
</thead>
<tbody>
<tr>
<td>1</td>
<td>0</td>
<td>Grails 3 - Step by Step</td>
<td>NOT_SET</td>
</tr>
<tr>
<td>2</td>
<td>0</td>
<td>Grails 3 - A practical guide to application development</td>
<td> BORROWED</td>
</tr>
<tr>
<td>3</td>
<td>0</td>
<td>Falando de Grails</td>
<td>SOLD</td>
</tr>
</tbody>
</table>

**Do you like to read about Grails / Geb / Groovy development?** If the answer is yes, subscribe to [Groovy Calamari](http://groovycalamari.com). A weekly curated email newsletter about the Groovy ecosystem. Curated by me 🎉