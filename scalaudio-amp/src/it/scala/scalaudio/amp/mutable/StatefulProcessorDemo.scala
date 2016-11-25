package scalaudio.amp.mutable

import signalz.StatefulProcessor

import scala.concurrent.duration._
import scalaudio.amp.immutable.filter.{DelayFilterState, DelayFilterStateGen}
import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 7/11/16.
  */
class StatefulProcessorDemo extends ScalaudioCoreTestHarness {
  "StatefulProcessor" should "produce sine without var in outer scope" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val ff = StatefulProcessor(SineStateGen.nextState, OscState(0, 440.Hz, 0)).nextState
      .map(state => Array(state.sample))

    AmpOutput(ff).play(5.seconds)
  }

  "StatefulProcessor" should "use pre-transformer for automation" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val preTransformer = (s : OscState) => s.copy(
      pitch = (s.pitch.toHz + .2).Hz
    )
    val ff = StatefulProcessor(SineStateGen.nextState,
      OscState(0, 440.Hz, 0),
      Some(preTransformer)
    ).nextState map (state => Array(state.sample))

    AmpOutput(ff).play(5 seconds)
  }

  "StatefulProcessors" should "be chainable" in {

    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var holder: Double = 0
    val delayFilter = StatefulProcessor(DelayFilterStateGen.nextState,
      DelayFilterStateGen.initialState(1.second),
      Some(
        (delayFilterState: DelayFilterState) => delayFilterState.copy(sample = holder)
      )
    )

    val ff = StatefulProcessor(SineStateGen.nextState, OscState(0, 440.Hz, 0)).nextState
      .map(_.sample)
      .map { sample =>
        holder = sample
        delayFilter.nextState().sample
      } map (Array(_))

    AmpOutput(ff).play(5.seconds)
  }
}
