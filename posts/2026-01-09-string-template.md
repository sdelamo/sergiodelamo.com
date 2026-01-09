---
title: StringTemplate
summary: StringTemplate is a Java template engine for generating source code, web pages, emails, or any other formatted text output.
date_published: 2026-01-09T09:49:03+01:00
keywords:template,java
external_url: https://www.stringtemplate.org
---

# [%title]

While reading [Spring AI in Action](https://www.manning.com/books/spring-ai-in-action), I discovered [StringTemplate](https://www.stringtemplate.org).

> StringTemplate is a Java template engine for generating source code, web pages, emails, or any other formatted text output. 
> StringTemplate is particularly good at code generators, multiple site skins, and internationalization / localization. StringTemplate also powers ANTLR.

- [Website](https://www.stringtemplate.org)  
- [GitHub Repository](https://github.com/antlr/stringtemplate4)
- [IntelliJ Plugin](https://plugins.jetbrains.com/plugin/8041-stringtemplate-v4)  

I am thinking of adding support for StringTemplate in [Micronaut Views](https://micronaut-projects.github.io/micronaut-views/latest/guide/). 

```java
String template = """
        You are a helpful assistant, answering questions about tabletop games.
        If you don't know anything about the game or don't know the answer,
        say "I don't know".
        
        The game is {game}.
        
        The question is: {question}.""";
String prompt = new ST(template, '{', '}')
        .add("game", question.gameTitle())
        .add("question", question.question())
        .render();
```