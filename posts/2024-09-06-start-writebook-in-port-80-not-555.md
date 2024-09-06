---
title: How to run Writebook in port 80 instead of 5555
summary:  Edit hostconfig.json to bind host port 80 to the writebook docker container.
date_published: 2024-09-06T14:06:42+01:00
  keywords:writebook,docker

---

# [%title]

I have installed [Writebook](https://once.com/writebook) in an Oracle Cloud instance.
Writebook uses Docker, and it gets installed by default in host port 5555. However, I want to run Writebook in port 80 of the host because it makes it easier to expose it to the Internet from an Oracle Cloud instance.

- Stop Docker
- Edit `hostconfig.json` for your container hash `sudo vi /var/lib/docker/containers/CONTAINERHASH/hostconfig.json` and replace `"HostPort":"5555"` with replace `"HostPort":"80"`.
- Start Docker.

Courtesy of this great [Stackoverflow answer](https://stackoverflow.com/a/38783433).
 