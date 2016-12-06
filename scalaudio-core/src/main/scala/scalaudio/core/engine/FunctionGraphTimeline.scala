package scalaudio.core.engine

import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/5/16.
  */
trait GraphTimeline {
  def play(duration: AudioDuration)
  def playWhile(loopCondition: () => Boolean)
}

// TODO: Just use current time from AudioContext since thats already available to functions?
case class FunctionGraphTimeline(signalGraph: () => _) extends GraphTimeline {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration) =
    1L to duration.toSamples foreach { s =>
      signalGraph()
      currentTime = s
    }

  def playWhile(loopCondition: () => Boolean) =
    while (loopCondition()) {
      signalGraph()
      currentTime += 1
    }

}

case class StreamGraphTimeline(stream: Stream[_]) {
  var currentTime: Long = 0 // Current time in samples

  def play(duration: AudioDuration) =
      stream.map(_ => currentTime += 1).take(duration.toSamples.toInt)

  def playWhile(loopCondition: Unit => Boolean) =
    stream.map(_ => currentTime += 1).takeWhile(loopCondition)
}

// Syntax simplifier
object Timeline {
  def functionGraph(signalGraph: () => _) = FunctionGraphTimeline(signalGraph)
  def streamGraph(stream: Stream[_]) = StreamGraphTimeline(stream)
}