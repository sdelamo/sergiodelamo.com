---
title: AppleScript - Export multiple Word documents as PDFs 
summary: The AppleScript script prompts you to select multiple word documents, asks you for a folder to save the PDFs, opens each document in Word and saves it as PDF. 
banner_image: https://images.sergiodelamo.com/applescript-word-to-pdf.png
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-07-21T16:23:19+01:00
date_modified: 2021-07-21T16:23:19+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #automation #pdf #word #applescript

I was recently asked to review a Book which was shared with me as a series of Word documents. However, I would like them as PDF. I would like to read and annotate them with an iPad and [PDF Expert](https://pdfexpert.com). 

AppleScript to the rescue!. The following AppleScript prompts you to select multiple word documents, asks you for a folder to save the PDFs, opens each document in Word and saves it as PDF. 
 
[%summary]

```applescript
tell application "Microsoft Word"
set wordDocuments to choose file with prompt "Please select the document to process:" with multiple selections allowed
set exportLocation to choose folder with prompt "Please select where you'd like export the pdfs:"
repeat with a from 1 to number of wordDocuments
	set doc to open item a of wordDocuments
	set theActiveDoc to the active document
	set docName to name of theActiveDoc
	save as theActiveDoc file format format PDF file name ((exportLocation as text) & docName & ".pdf")
	close theActiveDoc
end repeat
quit
end tell
````
