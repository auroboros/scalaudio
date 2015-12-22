scalaudio

To Do:
- Signal Chain needs to support more varied flows (replaceable frameFunc? Then do constructor args need to be passed in at all?)
- Audio input UnitGen needs to be fixed/tested (should be on own thread?)
- Sawtooth needs to be fixed (octave?)

Questions:
- Is internal caching of buffers really necessary if intention is to use flow of building frameFuncs to feed anonymous UnitGens?