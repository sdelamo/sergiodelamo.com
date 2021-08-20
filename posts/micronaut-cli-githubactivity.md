---
title: Micronaut CLI Github Activity
banner_image: https://images.sergiodelamo.com/micronaut-cli-github-activity.png
summary: A CLI application to help you gather what you did in Github
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-20T18:21:28+01:00
date_modified: 2021-08-20T18:21:28+01:00
keywords:micronaut
---

# [%title]

At [OCI](https://objectcomputing.com/), we report what we have been working on each day. Thus, I have to keep track of everything I have been doing. However, in a typical day jump from project to project, from issue to issue, in the [micronaut-projects](https://github.com/micronaut-projects) Github organization. It is challenging to keep track of everything that I have been doing.  

To help me, I have written a Micronaut CLI application which queries the [Github events API](https://docs.github.com/en/rest/reference/activity#events). It is available in the repository [sdelamo/githubactivity](https://github.com/sdelamo/githubactivity).

You can build the CLI and get usage information:

 ```bash
$ ./gradlew build
$ java -jar build/libs/githubactivity-0.1-all.jar 
Missing required option: '--token'
Usage: ghactivity [-hV] -t [-d=<days>] [-o=<organization>] [--type=<type>]
                  [-u=<user>]
...
  -d, --days=<days>   number of days for which to fetch events
  -h, --help          Show this help message and exit.
  -o, --org, --organization=<organization>
                      Github organization
  -t, --token         Github personal Token
      --type=<type>   Github's repository type
  -u, --user=<user>   Github's username
  -V, --version       Print version information and exit.
  ```
  
To get a list of what you did in the past 7 days:

```bash
% java -jar build/libs/githubactivity-0.1-all.jar -d 7 -t
Enter value for --token (Github personal Token): XXXX
.
...
....
# 2021-08-13
## micronaut-security
754 doc: since 2.5. security rule cannot access Body
749 doc: add back notes about deprecated methods removal
725 test: remove pending feature
## micronaut-guides
431 Update rotation publication Guide
## micronaut-build
182 fix: broken ConfigurationProperties Nested links
180 Links to ConfigurationProperties table header for Nested classes are broken 
179 Bump asciidoctorj from 1.5.6 to 2.5.2
178 Bump ncipollo/release-action from 1.8.6 to 1.8.8
176 Bump spotless-plugin-gradle from 5.12.5 to 5.14.2
175 Bump jsoup from 1.7.3 to 1.14.1
170 Bump jruby-complete from 1.7.26 to 9.2.19.0
167 Bump snakeyaml from 1.28 to 1.29
165 Bump gradle/wrapper-validation-action from 1.0.3 to 1.0.4
164 Bump actions/cache from 2.1.5 to 2.1.6
```

The CLI queries every repo in the organization and collects your activity.

If you work a lot with Github projects, this CLI application may come in handy. 
