package scalaudio.units.filter

import org.scalatest.{FlatSpec, Matchers}
import signalz.{StatefulProcessor, StreamingProcessor}

import scala.concurrent.duration._
import scalaudio.core.engine.AudioStreamGraph
import scalaudio.core.types.Frame
import scalaudio.core.{AudioContext, CoreSyntax, types}
import scalaudio.units.ugen.{OscState, ImmutableSine}
import scalaz.Scalaz._
import scalaz._

/**
  * Created by johnmcgill on 5/29/16.
  */
class FilterDemo extends FlatSpec with Matchers with CoreSyntax {
  "Gain & splitter" should "be chainable on sine with outer var" in {
    implicit val audioContext = AudioContext()

    var sineState = OscState(0, 440.Hz, 0)
    var splitterOut: Frame = Array.empty[Double]

    val frameFunc = () => {
      sineState = ImmutableSine.nextState(sineState)
      splitterOut = SplitFilter.split(2)(sineState.sample)
      GainFilter.applyGainToFrame(.05)(splitterOut)
    }

    frameFunc.play(5 seconds)
  }

  "Gain & splitter" should "be chainable on sine with StatefulProcessor" in {
    implicit val audioContext = AudioContext()

    val frameFunc = StatefulProcessor(ImmutableSine.nextState, OscState(0, 440.Hz, 0)).nextState
      .map(_.sample)
      .map(SplitFilter.split(2))
      .map(GainFilter.applyGainToFrame(.05))

    frameFunc.play(5 seconds)
  }

  "Gain & splitter" should "be chainable on sine with StreamingProcessor" in {
    implicit val audioContext = AudioContext()

    def stream = StreamingProcessor(ImmutableSine.nextState, OscState(0, 440.Hz, 0)).outStream
      .map(_.sample)
      .map(SplitFilter.split(2))
      .map(GainFilter.applyGainToFrame(.05))

    stream.play(5 seconds)
  }
}
