---
title: Start Orbit Timers with AppleScript
banner_image: https://images.sergiodelamo.com/start-orbit-timers-with-applescript.png
summary: Orbit is not Scriptable but you can workaround it
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-11T07:07:21+01:00
date_modified: 2021-08-11T07:07:21+01:00
keywords:automation,applescript,orbit
---

# [%title]

My [Harvest](https://www.getharvest.com/) yearly subscription is coming to an end. Harvest lacks iPad OS and Watch OS applications. They don't have an iOS widget. Moreover, it seems they are not really committed to native applications for Apple Platforms.

I heard about [Orbit](https://timeinorbit.com), a time tracking app, in the latest episode of [App Stories Podcast](https://appstories.net/episodes/235/); an interview with [Malin Sundberg](https://twitter.com/malinsundberg) creator of Orbit.

Unfortunately, Orbit is not scriptable. Neither is [Harvest](https://sergiodelamo.com/blog/tag/harvest.html).

However, I managed to write AppleScript scripts to automate things. 

The following script stops the current timer

```applescript
tell application "System Events"
tell process "Orbit"
	set frontmost to true
	keystroke "." using command down
end tell
end tell
```

I leaverage Orbit's shortcut `CMD + .` to stop the current timer. 

Starting a timer programmatically is more tricky. I had to do a some `set uiElems to entire contents` debugging 😓.

```applescript
on run argv
if (count of argv) > 2 then
	set theClient to item 1 of argv
	set theProject to item 2 of argv
	set theCategory to item 3 of argv
	if (count of argv) > 3 then
		set theNote to item 4 of argv
	else
		set theNote to ""
	end if
	tell application "System Events"
		tell process "Orbit"
			set frontmost to true
			keystroke "n" using command down
			delay 0.1
			set popupButtonName to "Client:"
			set targetMenuItemName to theClient
			tell pop up button popupButtonName of sheet 1 of window "Orbit"
				click
				set menuItemNames to name of every menu item of menu 1
				repeat with i from 1 to (menuItemNames count)
					set menuItemName to (item i of menuItemNames)
					if menuItemName contains targetMenuItemName then
						click menu item menuItemName of menu 1
					end if
				end repeat
			end tell
			delay 0.1
			set popupButtonName to "Project:"
			set targetMenuItemName to theProject
			tell pop up button popupButtonName of sheet 1 of window "Orbit"
				click
				set menuItemNames to name of every menu item of menu 1
				repeat with i from 1 to (menuItemNames count)
					set menuItemName to (item i of menuItemNames)
					if menuItemName contains targetMenuItemName then
						click menu item menuItemName of menu 1
					end if
				end repeat
			end tell
			delay 0.1
			set popupButtonName to "Category:"
			set targetMenuItemName to theCategory
			tell pop up button popupButtonName of sheet 1 of window "Orbit"
				click
				set menuItemNames to name of every menu item of menu 1
				repeat with i from 1 to (menuItemNames count)
					set menuItemName to (item i of menuItemNames)
					if menuItemName contains targetMenuItemName then
						click menu item menuItemName of menu 1
					end if
				end repeat
			end tell
			delay 0.1
			if theNote is not "" then
				set focused of text area 1 of scroll area 1 of sheet 1 of window "Orbit" of application process "Orbit" of application "System Events" to true
				keystroke theNote
				set focused of text area 1 of scroll area 1 of sheet 1 of window "Orbit" of application process "Orbit" of application "System Events" to false
				delay 0.1
			end if
			click button "Start" of sheet 1 of window "Orbit" of application process "Orbit" of application "System Events"
		end tell
	end tell
else
	set result to text returned of (display dialog "You need three arguments to start Orbit")
end if
end run
```

I start timers for example from [Bunch files](https://bunchapp.co)

```yaml
---
title: 🔔 Github
---

....
...
.

* ~/Library/Scripts/Orbit\ Start\ Timer.scpt "OCI" "Micronaut Product Development" "Github"
```

See it in action:

![AppleScript Orbit](https://images.sergiodelamo.com/orbit-apple-script.gif)