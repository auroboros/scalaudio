package scalaudio.units.synth

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.concurrent.duration._
import scalaudio.core.engine.FunctionGraph
import scalaudio.core.types.{AudioDuration, Pitch}
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
import scalaudio.units.control.AdsrEnvelope
import scalaudio.units.ugen.Sine

/**
  * Created by johnmcgill on 5/30/16.
  */
class MonosynthDemo extends ScalaudioCoreTestHarness {
  "Monosynth" should "jam some beefy sinewaves" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))
    val adsrTemplate = AdsrEnvelope(30.millis,
      .95,
      40.millis,
      .5,
      60.millis,
      15.millis)

    val notes: SortedMap[AudioDuration, (Pitch, AdsrEnvelope)] =
      TreeMap(
        (1.second: AudioDuration) ->(440.Hz, adsrTemplate),
        (2.second: AudioDuration) ->(550.Hz, adsrTemplate),
        (3.second: AudioDuration) ->(660.Hz, adsrTemplate),
        (4.second: AudioDuration) ->(880.Hz, adsrTemplate)
      )

    // TODO: Express this in streaming or stateful processor form

    var monosynthState = Monosynth.decodeInitialState(notes)

    val frameFunc = () => {
      monosynthState = Monosynth(Sine).nextState(monosynthState)
      Array(monosynthState.sample)
    }

    FunctionGraph(frameFunc).play(5 seconds)
  }
}
