#scalaudio

scalaudio is a library is to facilitate audio synthesis/analysis on the JVM by wrapping the Java Sound API in more productive syntax. Its goal is to provide extreme flexibility while reducing verbosity of audio processing code, with the added bonus of type-checking. It aims to be pretty modular (DI for config and output engines via implicits) yet use the same syntax for both real-time and offline processing. Efficiency is a secondary goal, though even in current form some mutable data structures are used to avoid performance snags of constant memory allocation.

###Modules
* core -- engine utils & a bunch of unitgens/processors based on traditional buffer-by-buffer processing
* [scalaudioAMP](https://github.com/auroboros/scalaudio/tree/master/scalaudio-amp) -- unitgens/processors focused on sample-by-sample processing & based on immutable messaging pipelines for state simplification

###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
Please contribute & get in touch, it will make me way more likely to work on this project regularly instead of just doing some periodic commits between bouts of screwing around with machine learning & Clojure, heh.

There are some big holes in the codebase (higher-level composition, automating parameters handling signal vs. control rate, & more!) and I have ideas as to how to tackle them but would appreciate help & input if anyone's got the time.
