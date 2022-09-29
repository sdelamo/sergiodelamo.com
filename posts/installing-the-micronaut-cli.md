---
title: Install Micronaut CLI via SDKMAN.
summary: The easiest way to install the Micronaut CLI is to use SDKMan.
date_published: 2022-09-29T10:48:32+01:00
keywords:micronaut
---

# [%title]

[%summary]

[SDKMAN](https://sdkman.io)

> SDKMAN! is a tool for managing parallel versions of multiple Software Development Kits on most Unix-based systems. It provides a convenient Command Line Interface (CLI) and API for installing, switching, removing, and listing Candidates. 

## Install the Latest

To install the latest version of Micronaut CLI, run:


```
sdkman install micronaut
```

## List Micronaut CLI candidates

To get a list of the Micronaut CLI available versions, type:

```
sdk list micronaut
```

```
================================================================================
Available Micronaut Versions
================================================================================
 > * 3.7.1               3.1.0               2.2.1             * 1.2.5          
   * 3.7.0             * 3.0.3             * 2.2.0             * 1.2.4          
     3.6.4               3.0.2             * 2.1.4             * 1.2.3          
     3.6.3               3.0.1             * 2.1.3             * 1.2.2          
     3.6.2             * 3.0.0             * 2.1.2               1.2.1          
   * 3.6.1               3.0.0-RC1           2.1.1             * 1.2.0          
   * 3.6.0               3.0.0-M5          * 2.1.0               1.2.0.RC2      
     3.5.5               3.0.0-M2          * 2.0.3               1.2.0.RC1      
   * 3.5.4               3.0.0-M1            2.0.2               1.1.4          
   * 3.5.3             * 2.5.13            * 2.0.1               1.1.3          
   * 3.5.2               2.5.12            * 2.0.0               1.1.2          
   * 3.5.1               2.5.11            * 2.0.0.RC2         * 1.1.1          
   * 3.5.0               2.5.9             * 2.0.0.RC1           1.1.0          
     3.4.4               2.5.8             * 2.0.0.M3            1.1.0.RC2      
     3.4.3               2.5.7             * 2.0.0.M2            1.1.0.RC1      
     3.4.2               2.5.6             * 2.0.0.M1            1.1.0.M2       
     3.4.1             * 2.5.5             * 1.3.7               1.1.0.M1       
   * 3.4.0             * 2.5.4             * 1.3.6               1.0.5          
     3.3.4             * 2.5.3               1.3.5               1.0.4          
     3.3.3             * 2.5.1             * 1.3.4               1.0.3          
     3.3.1             * 2.5.0             * 1.3.3               1.0.2          
     3.3.0               2.4.4             * 1.3.2               1.0.1          
     3.2.7               2.4.3             * 1.3.1               1.0.0          
     3.2.6             * 2.4.2             * 1.3.0               1.0.0.RC3      
     3.2.5             * 2.4.1             * 1.3.0.RC1           1.0.0.RC2      
     3.2.4             * 2.4.0             * 1.3.0.M2            1.0.0.RC1      
     3.2.3             * 2.3.4             * 1.3.0.M1            1.0.0.M4       
     3.2.2               2.3.3               1.2.11              1.0.0.M3       
     3.2.1             * 2.3.2               1.2.10              1.0.0.M2       
     3.2.0             * 2.3.1               1.2.9               1.0.0.M1       
     3.1.4             * 2.3.0             * 1.2.8                              
     3.1.3             * 2.2.3             * 1.2.7                              
   * 3.1.1               2.2.2             * 1.2.6                              

================================================================================
+ - local version
* - installed
> - currently in use
================================================================================
```

## Install a specific version

You can install a particular version of Micronaut CLI with: 

`sdk install micronaut 3.6.4`

## To use a specific version

To use a specific version of Micronaut CLI in a terminal window: 

`sdk use micronaut 3.7.1`. 

You can check the version of Micronaut CLI you are running by typing the following command: 

## Verify the version of Micronaut CLI you are running

```
% mn --version
Micronaut Version: 3.7.1
```


