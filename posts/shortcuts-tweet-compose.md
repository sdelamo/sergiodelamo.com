---
title: Compose Tweet about a website with Shortcuts
summary: Fetch information of the page with Javascript
banner_image: https://images.sergiodelamo.com/compose-website-tweet.png
author.name: Sergio del Amo
author.url: https://sergiodelamo.com/me.html
author.avatar: https://images.sergiodelamo.com/smallavatar.png 
date_published: 2021-08-03T07:44:54+01:00
date_modified: 2021-08-03T07:44:54+01:00
---

# [%title]

By [%author.name] - [%date_published]

Tags: #automation #shortcuts #javascript #tweetbot #safari

The following Javascript snippet composes a Tweet. 

- It fetches the title `<meta name="description"` of a page.
- It does some replacements to mention some twitter profiles. 
- It uses the `<meta name="keywords"` as a list of [hashtags](https://help.twitter.com/en/using-twitter/how-to-use-hashtags).
- It appends the url of the website to the tweet. 

```javascript
function getMeta(metaName) {
  var metas = document.getElementsByTagName('meta');
  for (let i = 0; i < metas.length; i++) {
    if (metas[i].getAttribute('name') === metaName) {
      return metas[i].getAttribute('content');
    }
  }
  return '';
}
var tweet = document.title + '. ' + getMeta('description');
const replacements = new Map();
replacements.set('Código Bot', '@codigobotfm');
replacements.set('Micronaut', '@micronautfw');
replacements.set('Grails', '@grailsfw');
for (const [key, value] of replacements) {
	tweet = tweet.replace(key, value)
}
tweet += ' #' + getMeta('keywords').replace(',', ' #');
tweet += window.location.href;
```

Next, create a [Shortcut](https://support.apple.com/guide/shortcuts/welcome/ios).

> A shortcut is a quick way to get one or more tasks done with your apps. The Shortcuts app lets you create your own shortcuts with multiple steps. 

It is a simple three step shortcut: 

- It accepts _Safari web pages_
- Run a Javascript snippet. Copy the above snippet. Add a last line of code `completion(tweet);`
- Open [Tweetbot](https://tapbots.com/tweetbot) to specified account. 

![Shortcuts - Accept Safari Web Page - Run Javascript - Tweet](https://images.sergiodelamo.com/shortcut-accepts-safari-web-pages-run-javascript-tweetbot.jpg)
