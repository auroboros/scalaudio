scalaudio

To Do:
- Maybe there should be subtype of filter for those that support Signal / ControlRate controls?
- Need generic format for channel filling etc. in signal / ControlRate controls
- Should create "1st buffer" notion for supplying warning messages that aren't exceptions so its not repetitive?
- Signal Chain needs to support more varied flows (replaceable frameFunc? Then do constructor args need to be passed in at all?)
- Audio input UnitGen needs to be fixed/tested (should be on own thread?)
- Sawtooth needs to be fixed (octave?)

Big To Do:
- Convert durations in DSL from frames to milliseconds at least

Questions:
- Is internal caching of buffers really necessary if intention is to use flow of building frameFuncs to feed anonymous UnitGens?
- Could go fully functional & treat filters as Singleton if no state is involved. Could have UnitGens recurse with state so its not stored internally?
Only problem is this shifts responsible to user to manage buffer updates (only call outputbuffer once since there will be no record of currentFrame)
Could prep recursion and pass back that function to be executed? So "state" is more just passing around of closures?
- Consider how to treat off-by-one issues surrounding duration (should a duration of 10 span 11 frames, start doesn't count...? Is it worth the misnomer
if calculations are easier?)