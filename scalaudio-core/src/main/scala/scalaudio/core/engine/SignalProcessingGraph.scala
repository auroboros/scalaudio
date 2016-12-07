package scalaudio.core.engine

import signalz.{FunctionGraph, SignalProcessingGraph, StreamGraph}

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/5/16.
  */
trait AudioSignalProcessingGraph extends SignalProcessingGraph {
  def play(duration: AudioDuration) = run(duration.toSamples)

  def playWhile(loopCondition: Unit => Boolean) = runWhile(loopCondition)
}

// Context temporal state advancement built into graph type. good? bad?
case class AudioFunctionGraph(audioFunctionGraph: () => _)(implicit audioContext: AudioContext)
  extends FunctionGraph(() => {
    audioFunctionGraph()
    audioContext.advanceBySample()
  }) with AudioSignalProcessingGraph

class AudioStreamGraph(stream: => Stream[_])(implicit audioContext: AudioContext)
  extends StreamGraph(stream.map(_ => audioContext.advanceBySample()))
    with AudioSignalProcessingGraph

object AudioStreamGraph {
  def apply(stream: => Stream[_])(implicit audioContext: AudioContext) = new AudioStreamGraph(stream)
}