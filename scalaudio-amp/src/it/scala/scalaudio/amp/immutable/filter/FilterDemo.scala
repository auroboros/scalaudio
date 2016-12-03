package scalaudio.amp.immutable.filter

import org.scalatest.{FlatSpec, Matchers}
import signalz.{StatefulProcessor, StreamingProcessor}

import scala.concurrent.duration._
import scalaudio.amp.immutable.ugen.{OscState, Sine}
import scalaudio.core.engine.StreamCollector
import scalaudio.core.types.Frame
import scalaudio.core.{AudioContext, CoreSyntax}
import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 5/29/16.
  */
class FilterDemo extends FlatSpec with Matchers with CoreSyntax {
  "Gain & splitter" should "be chainable on sine with outer var" in {
    implicit val audioContext = AudioContext()

    var sineState = OscState(0, 440.Hz, 0)
    var splitterOut: Frame = Array.empty[Double]

    val frameFunc = () => {
      sineState = Sine.nextState(sineState)
      splitterOut = SplitFilter.split(2)(sineState.sample)
      GainFilter.applyGainToFrame(.05)(splitterOut)
    }

    StreamCollector(frameFunc).play(5 seconds)
  }

  "Gain & splitter" should "be chainable on sine with StatefulProcessor" in {
    implicit val audioContext = AudioContext()

    val frameFunc = StatefulProcessor(Sine.nextState, OscState(0, 440.Hz, 0)).nextState
      .map(_.sample)
      .map(SplitFilter.split(2))
      .map(GainFilter.applyGainToFrame(.05))

    StreamCollector(frameFunc).play(5 seconds)
  }

  "Gain & splitter" should "be chainable on sine with StreamingProcessor" in {
    implicit val audioContext = AudioContext()

    def stream = StreamingProcessor(Sine.nextState, OscState(0, 440.Hz, 0)).outStream
      .map(_.sample)
      .map(SplitFilter.split(2))
      .map(GainFilter.applyGainToFrame(.05))

    StreamCollector(stream).play(5 seconds)
  }
}
