title: Read a property from gradle.properties with a bash script
date: May 16, 2017
---

# [%title]

[%date] #gradle #bash

```bash
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