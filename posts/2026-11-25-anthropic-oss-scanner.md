---
title: Anthropic's OSS Scanner
summary: Anthropic's opt-in OSS Scanner emails model-generated vulnerability reports, with a reproducer and a candidate patch when it has one, to open-source maintainers who enroll and can triage them.
date_published: 2026-11-25T09:00:00+01:00
keywords: security,open-source,ai,java,micronaut,grails
---

# [%title]

[%summary]

I maintain Micronaut, and I commit to Grails. The expensive part of a security report is deciding whether the finding is a bug, a duplicate, or the framework doing what we documented.

[OSS Scanner](https://red.anthropic.com/oss-scanner) scans accepted projects periodically with Anthropic's strongest models, at no cost, and the reports go out before a person has reviewed them. The slower path is still [coordinated vulnerability disclosure](https://red.anthropic.com/2026/cvd/): a human reviews the finding first. By October 2026 they say they had given human review to over 6,000 reports. The [launch post](https://www.anthropic.com/research/launching-opt-in-vuln-finding-service-for-open-source) of 8 October 2026 describes the six months before that: more than 29,000 candidate vulnerabilities, about 6,000 of them triaged by hand, and nearly 5,000 unvalidated reports sent to maintainers who asked for the full set. The Frontier Red Team runs it with Safeguards Research, informed by their use of Claude to find vulnerabilities during [Project Glasswing](https://www.anthropic.com/glasswing).

## What you receive

After enrollment they email a bundle of reports to the primary contact. The pipeline double-checks bugs, proposes patches, and does root-cause analysis. A report is meant to include a description, a reproducer, and a proposed patch, and, when they can produce one, a bisection of when the bug was introduced. Later scans, with no published schedule, look for missed bugs and for bugs added since.

These findings do not start a 90-day disclosure clock. A report may be wrong, and they will not make a maintainer study one they have not studied themselves. If a human later validates it under CVD, the 90 days start from that notice. A future clock on some high-severity scanner reports would come with notice and an opt-out.

## How to try it

A core maintainer opens a pull request on [anthropics/oss-scanner](https://github.com/anthropics/oss-scanner) that adds `projects/<name>/project.yaml` from their [template](https://github.com/anthropics/oss-scanner/blob/main/templates/project.yaml). The first pull request needs their contributor license agreement.

You give them a git URL (`repo`, any host, optional `#branch` or `#tag`), a public `primary_contact`, and a Dockerfile that installs dependencies and builds the project. Building the image has network access. The audit does not, so the tests should pass inside the image. The service page calls this field `Dockerfile`. The template calls it `dockerfile`. Keep the file in your own repository if you want to change the build without another pull request.

Run `tools/validate.py` before the pull request. The README's `tools/check` does the offline build locally; only point it at a project you trust. A threat model is optional. Without `.oss-scanner/threat_model.md`, the scanner guesses at the corner cases. `report_guidelines.md` and `patch_guidelines.md`, at most 16 KiB each, only shape the writing, and a file that tries to suppress findings is ignored. `disabled: true` pauses mail, and deleting the directory returns you to CVD. An OpenPGP key encrypts mail to the primary contact only, with no CCs. GitHub private advisories stay off unless you opt in.

A person checks that you are a core maintainer, that the address is a real security contact, and that the Dockerfile covers that repository. They email if their build fails. A harness checks that each finding is in your code, not in a dependency. Acceptance is case by case. They look for established projects with a critical impact on infrastructure and user security, on a bar they compare to OSS-Fuzz. New projects and ordinary forks usually do not qualify. The service is for maintainers who already keep up with verified high and critical reports. They also say many projects are already overwhelmed.

## A framework such as Micronaut

I am not enrolling Micronaut here, and Anthropic does not say they would accept it. Other applications depend on the framework, and much of the code handles input those applications do not fully trust: HTTP, serialization, expressions, data access. An empty threat model is where intended API behavior gets reported as a bug.

On an early version, the penetration testers who review Anthropic's CVD findings checked 97 critical and high-severity results across 48 projects. Of those, 85 (88%) met the CVD bar. Of the other 12, 11 were real but duplicates, and one was invalid. Some maintainers have said the severity was inflated, or that the scanner misunderstood the threat model. I would write that file before reading a report, and I would want the same for Grails: a security contact we can publish, a written scope, and someone to triage the mail.

## Limits

A report may miss bugs, describe a non-bug, mis-rank severity, and propose a patch that breaks the build. Review it before you change code. Share it only with the project's maintainers, and keep unpatched details confidential until the issue is fixed or disclosed. Anthropic can refuse a project or stop the service, and there is no fee. A credit line is optional, in the form they show:

> Discovered by Anthropic's OSS Scanner, as vulnerability `ANT-2026-ABCD1234`.

[Claude for OSS](https://claude.com/contact-sales/claude-for-oss) offers maintainers free Claude Max 20x subscriptions. The [Cyber Verification Program](https://www.anthropic.com/news/cyber-verification-program) is for qualifying security professionals. [Claude Security](https://claude.com/product/claude-security) is the commercial product; OSS Scanner is the no-cost program, and they say its extra harnesses are token-hungry and experimental. Reply to a report with feedback, or write to [oss-scanner-questions@anthropic.com](mailto:oss-scanner-questions@anthropic.com) if you are not enrolled.

The sources are the [service page](https://red.anthropic.com/oss-scanner), the [launch post](https://www.anthropic.com/research/launching-opt-in-vuln-finding-service-for-open-source), the [terms](https://red.anthropic.com/oss-scanner/terms), and the [enrollment repository](https://github.com/anthropics/oss-scanner). Follow the README for the pull request. It is ahead of the service page on the guidelines files, `tools/check`, and GitHub advisories.
