package scalaudio.core.engine

import signalz.{FunctionGraph, SignalProcessingGraph, StreamGraph}

import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/5/16.
  */
trait AudioSignalProcessingGraph extends SignalProcessingGraph {
  def play(duration: AudioDuration) = run(duration.toSamples)

  def playWhile(loopCondition: Unit => Boolean) = runWhile(loopCondition)
}

// TODO: Just use current time from AudioContext since thats already available to functions?
// TODO: IF THIS IS NOT ADDRESSED, MAY BE MANY REGRESSIONS... but interesting to leave unaddressed & see where ties to global timeline are really unnecessary & can be easily cut...
case class AudioFunctionGraph(audioSignalGraph: () => _)
  extends FunctionGraph(audioSignalGraph)
    with AudioSignalProcessingGraph

class AudioStreamGraph(stream: => Stream[_])
  extends StreamGraph(stream)
    with AudioSignalProcessingGraph

object AudioStreamGraph {
  def apply(stream: => Stream[_]) = new AudioStreamGraph(stream)
}