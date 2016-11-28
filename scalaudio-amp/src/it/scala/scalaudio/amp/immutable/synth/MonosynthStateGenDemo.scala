package scalaudio.amp.immutable.synth

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.concurrent.duration._
import scalaudio.amp.immutable.control.AdsrEnvelope
import scalaudio.amp.immutable.ugen.SineStateGen
import scalaudio.core.engine.StreamCollector
import scalaudio.core.types.{AudioDuration, Pitch}
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 5/30/16.
  */
class MonosynthStateGenDemo extends ScalaudioCoreTestHarness {
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

    var monosynthState = MonosynthStateGen.decodeInitialState(notes)

    val frameFunc = () => {
      monosynthState = MonosynthStateGen.nextState(monosynthState, SineStateGen)
      Array(monosynthState.sample)
    }

    StreamCollector(frameFunc).play(5 seconds)
  }
}
