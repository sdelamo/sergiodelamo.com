---
title: Disable transaction wrapping with @MicronautTest
summary: Use transactional false to avoid wrapping the test execution in a transaction.  
date_published: 2021-11-22T13:23:15+01:00
keywords:micronaut,micronaut-data,micronau-test,junit
---

# [%title]

[Micronaut Test](https://micronaut-projects.github.io/micronaut-test/latest/guide/):

> By default, when using `@MicronautTest`, each `@Test` method will be wrapped in a transaction that will be rolled back when the test finishes. This behaviour can be changed by using the transactional and rollback properties.

For example, if you create an application with `data-jdbc`, `postgres` and `test-containers` features with [Micronaut Launch](https://launch.micronaut.io). 

With an entity: 

```java
package com.example;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotBlank;

@MappedEntity
public class Book {
    @Id
    @NonNull
    @NotBlank
    private final String isbn;

    @NonNull
    @NotBlank
    private final String name;

    public Book(@NonNull String isbn,
                @NonNull String name) {
        this.isbn = isbn;
        this.name = name;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
```

A JDBC repository: 

```java
package com.example;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface BookRepository extends CrudRepository<Book, String> {
}
```

And a Controller: 

```java
package com.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/books")
class BookController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/count")
    Number count() {
        return bookRepository.count();
    }
}
```

The following test passes only if you set `transactional=false` with `@MicronautTest`.

```java
package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false)
public class BookControllerTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    BookRepository bookRepository;

    @Test
    void bookCount() {
        String title = "Building Microservices";
        String isbn = "1491950358";
        Book book = bookRepository.save(new Book(isbn, title));
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/books/count")
                .accept(MediaType.TEXT_PLAIN);
        Integer count = client.retrieve(request, Integer.class);
        assertEquals(1, count);
        bookRepository.delete(book);
    }
}
```



