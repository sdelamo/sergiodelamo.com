---
title: AppleScript to stop Harvest Timer
summary: You can execute Javascript from AppleScript
banner_image: https://images.sergiodelamo.com/stop-harvest-timer-with-applescript.png
date_published: 2021-08-01T07:17:24+01:00
keywords:automation,applescript,harvest,bunch,javascript
---

# [%title]

I use [Harvest](https://harvestapp.com) for time tracking. 

Also, I use [Bunch](https://bunchapp.co). Almost all of my bunches start a Harvest timer. I want to stop the current timer when I close the bunch.

This is an excerpt of my _Blog_ bunch. 

```
...
..
.

* ~/Library/Scripts/Harvest.scpt "SAC" "Writing" "Writing post"  ~5

!<<#On Close
___
#[On Close]
* ~/Library/Scripts/Stop\ Harvest.scpt
```

Unfortunately, Harvest does not integrate with AppleScript. Moreover, they don't have a menu option to stop the current timer. Neither, a [Keyboard Shortcut](https://support.getharvest.com/hc/en-us/articles/360048180272-Mac-App-Keyboard-Shortcuts) to stop the current timer. You can stop the the timer by click the `H` in the menu item but I am not sure I an automate that. 

Thus, I resort to browser automation. The following AppleScript:

- Opens Safari
- Creates new tab and navigates to my harvest account. 
- Clicks the current timer stop button via Javascript. 
 
```applescript
set website to "https://softamo.harvestapp.com/time"
set theScript to "document.getElementsByClassName('js-stop-timer')[0].click();"
tell application "Safari"
    tell window 1
        set current tab to (make new tab with properties {URL:website})
    end tell
    delay 5
    do JavaScript theScript in current tab of first window
end tell
```