package scalaudio.amp.immutable.control

import scalaudio.amp.immutable.filter.GainFilter
import scalaudio.amp.immutable.ugen.{OscState, SquareStateGen}
import scalaudio.core.engine.{Playback, Timeline}
import scalaudio.core.engine.samplewise.AmpOutput
import com.scalaudio.core.types.AudioDuration
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
/**
  * Created by johnmcgill on 5/29/16.
  */
class LinearEnvelopeDemo extends FlatSpec with Matchers with CoreSyntax {
  "Square wave with linear env" should "ramp up over 5 seconds" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second : AudioDuration) -> LinearEnvelope(0, 1, 5.seconds)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = SquareStateGen.nextState(sineState)
      envState = EnvelopeStateGen.nextState(envState)
      GainFilter.applyGainToFrame(Array(sineState.sample), envState.value)
    }

    AmpOutput(frameFunc).play(7 seconds)
  }

  "Square wave with multiple ramps/points" should "fluctuate accordingly" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second : AudioDuration) -> LinearEnvelope(0, 1, 5.seconds),
      (9.seconds : AudioDuration) -> LinearEnvelope(1, .2, 3.seconds),
      (13.seconds : AudioDuration) -> PointEnvelope(.85)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = SquareStateGen.nextState(sineState)
      envState = EnvelopeStateGen.nextState(envState)
      GainFilter.applyGainToFrame(Array(sineState.sample), envState.value)
    }

    AmpOutput(frameFunc).play(15 seconds)
  }
}
