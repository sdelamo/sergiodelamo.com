---
title: Audio Hijack settings for Codigobot
summary: We record audio via Audio Hijack.These are my settings
date_published: 2021-11-29T10:36:09+01:00
keywords:podcast,audio-hijack
---

# [%title]

To record [CodigoBot Podcast](https://codigobot.com), we use [Audio Hijack](https://rogueamoeba.com/audiohijack/).

Create a new Audio Hijack session. 

- Configure 
- New Session

You need to create something such as: 

![](https://images.sergiodelamo.com/ahoverview.png)

## Your Configuration: 

### Input Device

![](https://images.sergiodelamo.com/ahyourinput.png)

Configure your Audio Device (for my that is Scarlett Solo USB). Each audio interface that you have, has potentially multiple channel. I selected both channels to be 1. 

### Recorder

![](https://images.sergiodelamo.com/ahyourrecorder.png)

Format the file is actually saved in. 

File:  I selected `Código Bot - Sergio -  Date Time` You should use your name instead of `Sergio`. 

Save To: I selected the path to `raw` in `media.codigobot.com`. For example: `/Users/sdelamo/github/codigobot/media.codigobot.com/raw`

Quality: High quality MP3

#### Advanced Recording Options

Format MP3  
Bit Rate: 256 Kbps  
Bit Rate Mode: Constant Bitrate  
Sample Rate: 48000 Hz  
Channels: Mono  


## Guest Configuration

### Skype

![](https://images.sergiodelamo.com/ahskype.png)

Application Source: Skype

Include audio input: no  
Fill playback gaps with silence: yes    
Limit audio capture: no    

## Output Device

Add an output device. select as Audio Device your headphones to you are able to hear your guest. 

![](https://images.sergiodelamo.com/ahoutput.png)

## Recorder

![](https://images.sergiodelamo.com/ahguestrecorder.png)

File name: `Código Bot - Kini -  Date Time` You should use your guest name instead of `Kini`. 

#### Advanced Recording Options

Format: MP3  
Bit Rate: 256 Kbps  
Bit Rate Mode: Constant Bitrate  
Sample Rate: 48000 Hz  
Channels: Mono 