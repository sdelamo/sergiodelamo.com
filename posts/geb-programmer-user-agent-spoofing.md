---
title: Geb Programmer: User Agent Spoofing
date: Jul 27, 2016 14:52
---

# [%title] 

#geb
 
As often happens on the internet there is already a website to answer your question. If you would like to know what’s your user agent just visit whatsmyuseragent.com

![](https://images.sergiodelamo.com/Screenshot_27_07_16_15_40.png)

If you run Geb with [PhantomJs](http://phantomjs.org/) your user agent will identify you as running PhantomJs. You may be blocked because of that. Fortunately it is easy to change the user agent of PhantomJs driver as illustrated in the next GebConfig.groovy file:

```groovy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
 
def capabilities = new DesiredCapabilities()
String fakeUserAgent = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:37.0) Gecko/20100101 Firefox/37.0'
capabilities.setCapability('phantomjs.page.settings.userAgent', fakeUserAgent)
 
environments {
 
    chrome {
        driver = { new ChromeDriver() }
    }
 
    firefox {
        driver = { new FirefoxDriver() }
    }
 
    phantomJs {
        driver = { new PhantomJSDriver() }
    }
}
```

I've a [Github repository](https://github.com/sdelamo/gebwebbot_useragent) with this code example. 