---
title: Apple Podcast Smart Banner
summary Add a HTML <meta> to your HTML
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-15T07:37:43+01:00
date_modified: 2021-08-15T07:37:43+01:00
keywords:podcast,codigobot
---

# [%title]

You can use [Apple Smart Banners](https://developer.apple.com/documentation/webkit/promoting_apps_with_smart_app_banners) not just for apps but also for podcasts.

> Smart App Banners vastly improve users’ browsing experience compared to other promotional methods. In iOS, Smart App Banners provide a consistent look and feel that users come to recognize. They trust that tapping the banner will take them to the App Store and not a third-party advertisement. They appreciate unobtrusive banners at the top of a webpage, instead of a full screen that interrupts their experience with the web content. And with a large and prominent Close button, a banner is easy to dismiss. When the user returns to the webpage, the banner doesn’t reappear.

![Código Bot Podcast Smart Banner](https://images.sergiodelamo.com/codigobot-apple-podcasts-smart-banner.jpg)

You can obtain the `app-id`, a nine-digit unique identifier, from the Apple Podcast Landing page. 

![Código Bot Podcast app-id](https://images.sergiodelamo.com/codigobot-apple-podcasts-app-id.png)

I added a HTML meta to the HTML head of [codigobot.com](https://codigobot.com) pages.

```html
...
  <head>
    <meta name="apple-itunes-app" content="app-id=1548425863">
    ....
    ...
    ..
```

Subscribe to [Código Bot](https://codigobot.com) with you podcast player of choice. 
