---
title: Useful docker commands
summary: Stop, remove contains, remove docker images.
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-09-12T06:56:55+01:00
date_modified: 2021-09-12T06:56:55+01:00
keywords:docker
---

# [%title]

**stop all containers:**

```bash
docker kill $(docker ps -q)
```

**remove all containers**

```bash
docker rm $(docker ps -a -q)
```

**remove all docker images**

```bash
docker rmi $(docker images -q)
```