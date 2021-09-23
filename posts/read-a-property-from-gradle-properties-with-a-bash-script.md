---
title: Read a property from gradle.properties with a bash script
date_published: 2017-05-16T11:07:00+01:00
keywords:gradle,bash
---

# [%title]

```
#!/usr/bin/env bash

GRADLE_PROPERTIES_FILE=gradle.properties

function getProperty {
    PROP_KEY=$1
    PROP_VALUE=`cat $GRADLE_PROPERTIES_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}
GRAILS_VERSION=$(getProperty "grailsVersion")
echo $GRAILS_VERSION
```

