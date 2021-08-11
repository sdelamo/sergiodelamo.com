---
title: KSUID (K-Sortable Unique IDentifier)
summary: A globally unique identifier similar to UUID, built from the ground-up to be "naturally" sorted by generation timestamp without any special type-aware logic.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-04-24T21:39:04+01:00
date_modified: 2021-04-24T21:39:04+01:00
keywords:java,ksuid
---

# [%title]

[KSUID](https://github.com/segmentio/ksuid):

> KSUID is for K-Sortable Unique IDentifier. It is a kind of globally unique identifier similar to a RFC 4122 UUID, built from the ground-up to be "naturally" sorted by generation timestamp without any special type-aware logic.

It was originally open-sourced as a Go Library by [Segment](https://segment.com).

A sorteable unique identifier is useful in scenarios where you need your primary keys to be sorteable. For example, if you need to generate keys for your items in [DynamoDB](https://aws.amazon.com/dynamodb/). In fact, I learned about KSUID while reading [DynamoDB Book](https://www.dynamodbbook.com)

There is a [Java implementation of Segment's KSUID library](https://github.com/akhawaja/ksuid). You can generate a KSUID String with: `new Ksuid().generate()`.

For example, if you have a `Contact` POJO:

```java

package example;

import com.amirkhawaja.Ksuid;

import java.io.IOException;

public class Contact implements Comparable<Contact> {

    private final String id;

    private final String name;

    private final String title;

    public Contact(String name, String title) throws IOException {
        this.id = new Ksuid().generate();
        this.name = name;
        this.title = title;
    }

    @Override
    public int compareTo(Contact o) {
        return o.getId().compareTo(getId());
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" +  id + " " + new Ksuid().parse(id) + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (id != null ? !id.equals(contact.id) : contact.id != null) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        return title != null ? title.equals(contact.title) : contact.title == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
```

You can sort it via the primary key:

```java
Contact tim = new Contact("Tim Cook", "CEO");
Contact katherine = new Contact("Katherine Adams", "Senior Vice President and General Counsel");
Contact eddy = new Contact("Eddy Cue", "Senior Vice President Internet Software and Services");
Contact craig = new Contact("Craig Federighi", "Senior Vice President Software Engineering");
List<Contact> contacts = new ArrayList<>();
contacts.add(craig);
contacts.add(eddy);
contacts.add(tim);
contacts.add(katherine);
            
Collections.sort(contacts);

assert contacts.get(0).equals(tim);
System.out.println(tim.toString());
```

The primary key contains the timestamp. The previous code prints: 

```java
Contact{id='DRIvC0YHDXBuGawemfYsenzBN84 Time: 2021-04-24T20:18:19Z[UTC]
Timestamp: 1619295499
Payload: [70, 7, 13, 112, 110, 25, -84, 30, -103, -10, 44, 122, 124, -63, 55, -50]', name='Tim Cook', title='CEO'}
```

