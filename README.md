scalaudio

To Do:
- Signal Chain needs to support more varied flows (replaceable frameFunc? Then do constructor args need to be passed in at all?)
- Audio input UnitGen needs to be fixed/tested (should be on own thread?)
- Sawtooth needs to be fixed (octave?)

Questions:
- Is internal caching of buffers really necessary if intention is to use flow of building frameFuncs to feed anonymous UnitGens?
- Could go fully functional & treat filters as Singleton if no state is involved. Could have UnitGens recurse with state so its not stored internally?
Only problem is this shifts responsible to user to manage buffer updates (only call outputbuffer once since there will be no record of currentFrame)
Could prep recursion and pass back that function to be executed? So "state" is more just passing around of closures?