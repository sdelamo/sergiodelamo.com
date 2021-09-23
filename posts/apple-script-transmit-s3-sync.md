---
title: AppleScript Transmit File Sync
summary: Trigger Synchronization a local folder to an S3 bucket with AppleScript
banner_image: https://images.sergiodelamo.com/applescript-transmit-file-sync.png
date_published: 2021-08-07T17:48:09+01:00
keywords:automation,applescript,transmit
---

# [%title]

I [host this website's media in a S3 bucket](https://sergiodelamo.com/blog/static-website-hosting-with-aws.html). 
Transmit handles FTP, SFTP, WebDAV but also cloud services such as S3, Google Drive, Dropbox, Microsoft Azure...

I use [File Sync](https://library.panic.com/transmit/transmit5/synchronize/) in [Transmit 5](https://panic.com/transmit/) to sync a local folder on my Mac with a S3 bucket. 

> Synchronization is a quick, fully automated method of bringing a folder up to date with the contents of another folder. 

The name of the Transmit's favorite is `images.sergiodelamo.com`. I wrote an AppleScript to trigger the sync:

```applescript
tell application "Transmit"
    activate    
    set myFave to item 1 of (favorites whose name is "images.sergiodelamo.com")    
    tell document 1
        tell current tab
            connect to myFave
            synchronize local browser to remote browser
            close remote browser
        end tell
    end tell    
    quit
end tell
```

