---
title: Don't Just Say "Review My Code"
summary: A first look at David Carrero's bilingual collection of structured AI code-review prompts, and how an open-source maintainer might use them on pull requests.
date_published: 2026-11-17T09:00:00+01:00
keywords: code-review,ai,open-source,java,micronaut
---

# [%title]

[%summary]

I review a lot of pull requests. Some are typo fixes. Others touch authentication, data access, or the public API of a framework that thousands of applications depend on. In the Micronaut and Grails communities, a merge is a promise that the change is safe, compatible, and something we are willing to maintain.

Like many maintainers, I have started pasting diffs into an AI assistant. "Review my code" is a lazy prompt, and the model usually answers in kind: vague style comments, generic warnings, and little I would actually leave on a pull request.

That is why [Awesome Code Review Prompts](https://github.com/dcarrero/awesome-code-review-prompts), a collection by [David Carrero Fernández-Baillo](https://github.com/dcarrero), caught my eye. It is a curated set of master prompts for asking an AI assistant to review code along the dimensions that actually matter in a merge: security, cybersecurity, performance, accessibility, usability, testing, AI/LLM safety, and code quality.

## What is in the repository

The project is public, MIT-licensed, and bilingual. Every prompt ships in English (`name.md`) with a Spanish twin (`name-ES.md`). The current release is **0.4.1**, from 16 July 2026. As of this writing (17 November 2026), the GitHub API reports 32 stars and 3 forks.

The 39 master prompts live under `prompts/` in three folders: nine language-agnostic prompts in **`general/`**, including an all-in-one "review board" that scores every relevant dimension and returns a single release verdict; fourteen language add-ons in **`languages/`**, among them a [Java](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/languages/java.md) file; and sixteen framework and DevOps add-ons in **`frameworks/`**. For Java, the framework file today is [Spring Boot](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/frameworks/spring-boot.md). There is no Micronaut add-on yet.

Add-ons are complements, not replacements, and [CONTRIBUTING.md](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/CONTRIBUTING.md) explains how to add a new stack as an EN/ES pair. CI checks pairing, links, and Markdown style.

Every prompt follows the same structure: a **role**, a **scope**, a **method**, a fixed **output contract**, and a request for up to three **clarifying questions** before the model starts judging. You copy the fenced `text` block, paste it into Claude, GPT, Gemini, Grok, or another frontier model, then add a `git diff` or a file. Optionally, you append a language or framework add-on.

## Three prompts I would actually use

### Application security

The [security](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/general/security.md) prompt casts the model as a senior application security engineer and walks a familiar OWASP-style checklist. What I like is the method: the model is told to trace untrusted data from source to sink, map findings to OWASP and CWE, and then:

> Do NOT report theoretical issues you cannot substantiate; avoid false positives.

That sentence alone is worth the paste. Unsolicited AI reviews drown you in hypotheticals. Asking the model for a severity, exact location, exploit scenario, and copy-ready fix is much closer to how I want a review to read. It also asks up to three questions *before* reviewing, so it gathers context instead of guessing.

### Testing and test quality

We also have to decide whether the tests in a PR would actually protect the change. The [testing](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/general/testing.md) prompt reviews the tests themselves, not the implementation:

> Your job is to judge whether these tests would actually catch regressions — not whether they pass. Assume a passing suite can still be worthless.

The scope covers behavior coverage, weak assertions, over-mocking, flakiness, and missing regression tests. The method is one I already use when I read a JUnit or Spock file: for each production risk, ask "would any test here fail if this broke?" Findings must explain the gap — "this would still pass if …" — and propose a concrete test. The verdict is equally direct: Trustworthy, Needs work, or Not trustworthy.

### The Java add-on

The [Java add-on](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/languages/java.md) is meant to be appended after a general prompt. It adds JVM-specific checks I would be happy to see on a Micronaut diff: `PreparedStatement` instead of concatenated SQL, unsafe deserialization, SSRF via `HttpClient`, thread safety, try-with-resources, swallowed exceptions, and JPA N+1 queries.

It also includes Spring-specific security checks (`@PreAuthorize`, CSRF, actuator exposure, SpEL injection). Those will not always apply to a Micronaut module. You would treat them as noise — or contribute a Micronaut add-on that talks about compile-time dependency injection, the HTTP server and client, Micronaut Data, and native-image constraints. The Java file is still a solid base.

## How I might use this on a Micronaut pull request

I am not claiming this is already our process. These are ideas worth trying.

For a typical pre-merge pass, the [how-to-use guide](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/docs/how-to-use.md) suggests pasting `git diff origin/main...HEAD` with the [all-in-one](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/general/all-in-one.md) prompt and asking only for the scorecard, the top fix, and the release verdict (`Ship` / `Ship with fixes` / `Do not ship`). Treat any Critical or High finding as blocking until a human has confirmed or dismissed it.

For a change that touches authentication, serialization, or an HTTP surface, I would stack the security prompt with the Java add-on. For a hot-path change, I would use the [performance](https://github.com/dcarrero/awesome-code-review-prompts/blob/main/prompts/general/performance.md) prompt and tell the model the data volumes and latency targets. For a PR that is mostly tests, I would point the testing prompt at the new tests *and* the code under test.

The guide also describes a "fix-forward" loop and a mentoring mode that asks the model to explain the *why*. Both feel well suited to open source. I would start manually, and only automate the combos that consistently help.

## Caveats

None of this replaces a human maintainer. An AI reviewer does not know our compatibility guarantees, our preference for compile-time over runtime magic, or the history behind a subtle API choice. It will still produce false positives. Findings are leads, not verdicts. The project's own guide says it well: "Trust but verify. Treat findings as expert leads, not gospel."

Context remains the most important input. A prompt that does not know whether it is looking at a prototype or at Micronaut Security will mis-rank issues. A focused diff works better than a 50-file dump, and you should not paste secrets or an unreleased vulnerability into a third-party model. If the model flags a missing `@PreAuthorize` on a Micronaut service, that is a cue to ignore it — or to write a better add-on.

## A useful collection, and a kind invitation

Awesome Code Review Prompts will not review the pull request for you. What it does is more modest, and more useful: it gives you a well-structured brief, a checklist you would have written yourself if you had the time, and an output format you can paste into a GitHub review or an issue.

The repository is at [github.com/dcarrero/awesome-code-review-prompts](https://github.com/dcarrero/awesome-code-review-prompts). It is MIT-licensed. Credit goes to David Carrero Fernández-Baillo, who maintains the collection and also published a [companion article](https://systemadministration.net/code-and-infrastructure-review-with-and-without-ai-a-practical-guide-for-system-administrators-and-developers/) on reviewing code and infrastructure with and without AI. If you find the prompts useful, a star on the repo is a well-deserved thank you. And if you work on Micronaut, Grails, or another JVM stack that is not listed yet, a carefully written add-on would be a nice contribution — in English and Spanish, as the project asks.
