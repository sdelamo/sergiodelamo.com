---
title: AppleScript - Open Mail to an specific inbox
summary: Avoid distractions and go to the email inbox you are working with.
banner_image: https://images.sergiodelamo.com/applescript-open-mail-inbox.png
date_published: 2021-10-13T11:28:20+01:00
keywords:automation,applescript,mail,macos
---

# [%title]

I use MacOS [Mail](https://support.apple.com/guide/mail/welcome/mac) to handle multiple email accounts. I often want to open my work inbox and avoid seeing the other email.  

The following AppleScript snippet opens my [OCI](https://objectcomputing.com) inbox: 

```applescript
tell application "Mail"

    activate

    set ociInbox to mailbox "INBOX" of account "OCI"

    tell message viewer 1
    
        set selected mailboxes to ociInbox
    
    end tell

end tell
```

I learned about this thanks to the webinar _Make Apple Mail dance_ webinar by [David Sparks](https://www.macsparky.com)