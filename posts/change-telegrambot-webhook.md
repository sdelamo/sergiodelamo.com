---
title: cURL command to set a Telegram chatbot's webhook
summary: To receive Telegram callbacks you have to set the webhook.
date_published: 2021-05-22T07:44:34+01:00
keywords:telegram,chatbot
---

# [%title]

[%summary]

```
curl -X "POST" "https://api.telegram.org/xxx/setWebhook" 
    -d '{"url": "https://yourapp.com/telegram/xxx"}'  
    -H 'Content-Type: application/json; charset=utf-8'
```

Replace `xxx` with the token which the [Telegram Bot Father](https://t.me/botfather) provided you.

Telegram recommends you to include your token as a path variable in the webhook's url. 

> If you'd like to make sure that the Webhook request comes from Telegram, we recommend using a secret path in the URL, e.g. https://www.example.com/<token>. Since nobody else knows your bot's token, you can be pretty sure it's us.




