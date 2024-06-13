---
title: What is the future of reactive programming?
summary: I recently watched an [AMA (Ask Me Anything) video](https://www.youtube.com/watch?v=9si7gK94gLo&t=1156s) between [Brian Goetz](https://twitter.com/briangoetz9), Java Language Architect at Oracle, and [Nicolai Parlog](https://nipafx.dev), Java Developer Advocate at Oracle, where Brian Goetz shared
  his opinion about the future of reactive programming.
date_published: 2024-06-13T10:49:43+01:00
keywords: java,reactive,virtual-threads
external_url: https://www.youtube.com/watch?v=9si7gK94gLo&t=1156s
---

# [%title]

[%summary]

For Brian, [Loom](https://openjdk.org/projects/loom/) will kill reactive programming, making it a transitional technology.
On the one hand, reactive programming allows you to have many asynchronous operations going on, way more than the number of threads you have.
On the other hand, you give up:
- Control flow statements.
- The ability to write simple loops.
- The ability to sequentially debug your code.
- The ability to get clear stack traces.

The reactive programming tradeoff is not worth it.

This is transcription generated with [Audio Hijack's Trascribe block](https://rogueamoeba.com/support/manuals/audiohijack/?page=transcribe): 

> **What do you see as the future of reactive programming in Java going forward?** 
>Yeah, so this is a great question. I'm going to give a controversial answer, which some people will like and some people will hate. 
> I think Loom is going to kill reactive programming. I think we are going to realize very quickly... that reactive programming was a transitional technology. 
> It was a reaction to a problem that we had, and when that problem is taken away, it will become obvious that this is not the solution, the reactive is not the solution we want, 
> and I'll explain, 'cause I don't wanna just make a provocative statement and then move on to a different topic. 
> The problem with Reactive is it gets you one good thing. It gets you this event-driven model that allows you to decouple computations from threads. 
> So you can have a lot of asynchronous operations going on, way more than the number of threads that you have. 
> But the cost of it is really high. Basically, you have to give up so much. many things that the language already gives you. 
> You give up control flow statements. 
> You give up being able to write simple loops. 
> You give up having, you know, being able to sequentially debug your code. 
> You give up being able to get clear stack traces of
> , how did I get to this point where, you know, I, where I experienced this error, all of those things come for free if you're programming with straightforward, you know, 
> sequential code, and reactive frameworks take that all away from you. 
> So they give you one good thing but they take away a bunch of good things that we were all used to and if your code doesn't work, good luck debugging it, and 
> what Loom does is it takes away a bunch of good things that we were all used to and if your code doesn't work, good luck debugging it. 
> it says, well, the real bug was not the programming model. The bug was that threads were too expensive, and so I could only create 1,000, 5,000, 10,000. 
> I can't create a million, and so the, if we just solve that problem of let's make it cheaper to create lots. threads, then people will write just ordinary straightforward sequential code, 
> and it will, using the control flow techniques they know from the language, and using the debuggers that they've already got in their toolbox, and getting clean stack traces 
> when there's an error, and it's going to be great, and I think a lot of people will look at React. and say, "Boy, that was, you know, that was a cost and benefit that were not in line." 
