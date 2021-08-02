---
title: Compose an email with AppleScript
summary: Automate repetitive emails
banner_image: https://images.sergiodelamo.com/compose-an-email-with-applescript.png
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-02T08:06:46+01:00
date_modified: 2021-08-02T08:06:46+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #automation #applescript

I have recently committed to send every Friday an update email to someone at work. Few lines need change from week to week. I have written a simple AppleScript script to help me do it. 


```applescript
set recipientName to "John Snow"
set recipientAddress to "jsnow@gameofthrones.example"
set theSubject to "Winter is Comming"
set theContent to "Hi 

I have seen white walkers in the following spots: 

XXX

Thanks,
Sergio"

tell application "Mail"
    
    set theMessage to make new outgoing message with properties {subject:theSubject, content:theContent, visible:true}
    
    tell theMessage
        make new to recipient with properties {name:recipientName, address:recipientAddress}
    end tell
end tell
```

Not every detail of the email is completely automated. Hence, I need to fill some details before clicking send. 

If you have every detail ready, you can send the emails directly from `AppleScript` with the `send` keyword.