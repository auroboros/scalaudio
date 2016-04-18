#scalaudio

scalaudio is a library is to facilitate audio synthesis/analysis on the JVM by wrapping the Java Sound API in more productive syntax. Its goal is to provide extreme flexibility while reducing verbosity of audio processing code, with the added bonus of type-checking. It aims to be pretty modular (can create different config contexts, strap on different output engines) yet use the same syntax for both real-time and offline processing.

###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
I welcome contributors and will happily review/accept PRs as well as entertaining discussion about fundamental, possibly far-reaching changes to the library core. It is currently somewhat functional but there is clearly some identity crisis concerning handling of sample-rate vs control rate signal, for example. (I do have some ideas for most of these issues but would love for a person with some ingenuity to try to leap-frog to something even better).

Some working examples will be uploaded ASAP (though the test folder gives some good hints, as many of those ARE working tests... just can't vouch for 100% passing at this time as the library underwent a lot of rapid evolutions in the first few months of work).

If you write and engage me I will be way more likely to continue work on this project, so please do!
