---
title: Testing websockets with WSCAT
summary: The wscat utility is a convenient tool for testing a WebSocket API
date_published: 2022-06-06T16:40:09+01:00
keywords:websocket
external_url: https://github.com/websockets/wscat
---

# [%title]

[%summary]


```
$ wscat -c ws://websocket-echo.com
Connected (press CTRL+C to quit)
> hi there
< hi there
> are you a happy parrot?
< are you a happy parrot?
```