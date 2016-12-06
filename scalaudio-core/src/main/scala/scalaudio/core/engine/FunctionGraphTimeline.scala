package scalaudio.core.engine

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/5/16.
  */
case class FunctionGraphTimeline(signalGraph: () => _) {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration)(implicit audioContext: AudioContext) =
    1L to duration.toSamples foreach { s =>
      signalGraph()
      currentTime = s
    }

  def playWhile(loopCondition: () => Boolean)(implicit audioContext: AudioContext) =
    while (loopCondition()) {
      signalGraph()
      currentTime += 1
    }

}

case class StreamGraphTimeline(stream: Stream[_]) {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration)(implicit audioContext: AudioContext) =
      stream.map(_ => currentTime += 1).take(duration.toSamples.toInt)

  def playWhile(loopCondition: Unit => Boolean)(implicit audioContext: AudioContext) =
    stream.map(_ => currentTime += 1).takeWhile(loopCondition)
}

// Syntax simplifier
object Timeline {
  def functionGraph(signalGraph: () => _) = FunctionGraphTimeline(signalGraph)
  def streamGraph(stream: Stream[_]) = StreamGraphTimeline(stream)
}