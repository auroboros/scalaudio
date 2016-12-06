package scalaudio.core.engine

import scalaudio.core.types.{AudioDuration, Frame}

/**
  * Created by johnmcgill on 12/5/16.
  */
trait SignalProcessingGraph {
  def play(duration: AudioDuration)
  def playWhile(loopCondition: Unit => Boolean)
}

// TODO: Just use current time from AudioContext since thats already available to functions?
case class FunctionGraph(signalGraph: () => _) extends SignalProcessingGraph {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration) =
    1L to duration.toSamples foreach { s =>
      signalGraph()
      currentTime = s
    }

  def playWhile(loopCondition: Unit => Boolean) =
    while (loopCondition()) {
      signalGraph()
      currentTime += 1
    }

}

case class StreamGraph(stream: Stream[_]) extends SignalProcessingGraph {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration) =
      stream.map(_ => currentTime += 1).take(duration.toSamples.toInt)

  def playWhile(loopCondition: Unit => Boolean): Unit =
    stream.map(_ => currentTime += 1).takeWhile(loopCondition)
}

// Syntax simplifier
object Timeline {
  def functionGraph(signalGraph: () => _) = FunctionGraph(signalGraph)
  def streamGraph(stream: Stream[_]) = StreamGraph(stream)
}