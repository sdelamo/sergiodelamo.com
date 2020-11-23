---
title: Preview of Groovy 3
date_published: 2017-04-24T09:34:00+01:00
date_modified: 2017-04-24T09:34:00+01:00
---

# [%title]

[%date]

Tags: #groovy

This blog post is a translation of [@daniel_sun](https://twitter.com/daniel_sun)'s post, originally [posted in Chinesse](http://blog.sunlan.me/2017/04/15/Groovy-3之新特性预览/).

The next section will focus on the new parser “Parrot" features coming to Groovy 3. If you wish to be able to get yourself back and forth, follow the steps below to start Groovy 3's groovy console to try these new features:

```groovy
$ git clone https://github.com/danielsun1106/groovy-parser.git
$ cd groovy-parser
$ ./gradlew groovyConsole
```

## New feature preview

### Improve the loop statement

Prior to Groovy 3, Groovy neither supported the do-while statement nor supported statements that really conform to the Java syntax specification (such as multiple initialization and multiple update expressions). In Groovy 3, complete support for looping statements makes Java background developers smoother over Groovy development. Give some examples:

### Do-while statement example

```groovy
int i = 0;
do {
    i++
} while (i &lt; 5)
assert i == 5
```

An example of a for statement that conforms to the Java syntax specification

```groovy
def result = 0
for (int i = 0, j = 0; i &lt; 5 &amp;&amp; j &lt; 5; i = i + 2, j++) {
    result += i;
    result += j;
}
assert 9 == result
```

## 2. Supports Lambda expressions

Java 8 introduces the Lambda expression, in order to better compatible with Java syntax, Groovy 3 also added support for the syntax features. Give some examples:

### Lambda expression example

```groovy
assert 9 == [1, 2, 3].stream().map(e -&gt; e + 1).reduce(0, (r, e) -&gt; r + e)
```

## 3. Support method reference (reference) and constructor reference (constructor reference)

In addition to introducing Lambda expressions, Java 8 introduces the method reference and the constructor reference, and for the same reason, Groovy 3 adds support for the syntax feature. Give some examples:

### Method reference

```groovy
assert ['A', 'B', 'C'] == ['a', 'b', 'c'].stream().map(String::toUpperCase).collect(Collectors.toList())
```

### Constructor reference example

```groovy
assert [1, 2, 3] as String[] == [1, 2, 3].stream().map(String::valueOf).toArray(String[]::new)
```

## 4. Support for try-with-resources statements

Java 7 introduced the automatic resource management mechanism, developers can try-with-resources statement is very easy to complete the management of resources. It is also supported in Groovy 3. Give some examples:

### Try-with-resources statement example

```groovy
class Resource implements Closeable {
    int resourceId;
    static closedResourceIds = [];
    public Resource(int resourceId) {
        this.resourceId = resourceId;
    }
    public void close() {
        closedResourceIds &lt;&lt; resourceId
    }
}
def a = 1;
try (Resource r1 = new Resource(1)) {
    a = 2;
}
assert Resource.closedResourceIds == [1]
assert 2 == a
```

## 5. Support for code blocks

There is an infrequent but useful syntax in Java that can easily isolate the scope of the variable, the code block. Groovy 3 also supported it. Give some examples:

### Code block example

```groovy
{
    def a = 1
    a++
    assert 2 == a
}
{
    def a = 1
    assert 1 == a
}
```

## 6. Support Java-style array initialization

In order to better compatible with Java syntax, Groovy 3 added support for Java-style array initialization.

### Array initialization example

```groovy
def a = new int[] {1, 2}
    assert a[0] == 1
    assert a[1] == 2
    assert a as List == [1, 2]
```

## 7. support interface default method (default method)

Among the many new features introduced in Java 8, the default method of interface is also a useful feature. Groovy 3 naturally will not omit its support. Give some examples:

### Default method

```groovy
interface Greetable {
    String name();
    default String hello() {
        return 'hello'
    }
    default public String sayHello() {
        return this.hello() + ', ' + this.name()
    }
}
class Person implements Greetable {
    @Override
    public String name() {
        return 'Daniel'
    }
}
def p = new Person()
assert 'hello, Daniel' == "${p.hello()}, ${p.name()}"
assert 'hello, Daniel' == p.sayHello()
```

## 8. New operator: consistency operator (===,! ==), Elvis assignment (? =),! In,! Instanceof

In order to make the Groovy program more concise, Groovy 3 introduced these new operators. Give some examples:

The consistency operator (===,! ==) example
assert 'abc' === 'abc'
assert 'abc' !== new String('abc')
Elvis assignment (? =) Example

```groovy
def a = 2
a ?= 1
assert a == 2
a = null
a ?= 1
assert a == 1
```

### !in Example

```groovy
assert 1 !in [2, 3, 4]
```

### !instanceof Example

```groovy
assert 1 !instanceof String
```

## 9. Support for secure retrieval

Groovy has a safe way of doing so, that is, references to. This avoids the occurrence of NullPointerException, but lacks support for secure retrieval of collections and arrays, and Groovy 3 makes up for this shortcoming. Give some examples:

### Safe search example

```groovy
String[] array = ['a', 'b'];
assert 'b' == array?[1];
array?[1] = 'c';
assert 'c' == array?[1];
array = null;
assert null == array?[1];
array?[1] = 'c';
assert null == array?[1];
```

## 10. Supports runtime Groovydoc and saves Groovydoc as metadata in the AST node

Groovy 3 adds runtime Groovydoc, which simply adds @Groovydoc at the beginning of the Groovydoc content to access the Groovydoc content at run time. If Javadoc is accompanied by Java source code is a reflection of the document, then the run-time Groovydoc is further, it is stored in bytecode, and the program has done a real combination.

### Runtime Groovydoc example

```groovy
/**
 * @Groovydoc
 * class AA
 */
class AA {
    /**
     * @Groovydoc
     * field SOME_FIELD
     */
    public static final int SOME_FIELD = 1;
    /**
     * @Groovydoc
     * constructor AA
     */
    public AA() {
    }
    /**
     * @Groovydoc
     * method m
     */
    public void m() {
    }
    /**
     * @Groovydoc
     * class InnerClass
     */
    class InnerClass {
    }
}
/**
 * @Groovydoc
 * annotation BB
 */
@interface BB {
}
assert AA.class.getAnnotation(groovy.lang.Groovydoc).value().contains('class AA')
assert AA.class.getMethod('m', new Class[0]).getAnnotation(groovy.lang.Groovydoc).value().contains('method m')
assert AA.class.getConstructor().getAnnotation(groovy.lang.Groovydoc).value().contains('constructor AA')
assert AA.class.getField('SOME_FIELD').getAnnotation(groovy.lang.Groovydoc).value().contains('field SOME_FIELD')
assert AA.class.getDeclaredClasses().find {it.simpleName.contains('InnerClass')}.getAnnotation(groovy.lang.Groovydoc).value().contains('class InnerClass')
assert BB.class.getAnnotation(groovy.lang.Groovydoc).value().contains('annotation BB')
```

An example of saving Groovydoc as a metadata in an AST node

```groovy
import org.codehaus.groovy.control.*
import static org.apache.groovy.parser.antlr4.GroovydocManager.DOC_COMMENT
def code = '''
/**
 * Groovydoc for hello method
 * @author Daniel Sun
 */
void hello(String name) {}
'''
def ast = new CompilationUnit().tap {
    addSource 'hello.groovy', code
    compile Phases.SEMANTIC_ANALYSIS
}.ast
assert ast.classes[0].methods.grep(e -&gt; e.name == 'hello')[0].nodeMetaData[DOC_COMMENT].contains('Groovydoc for hello method')
```

