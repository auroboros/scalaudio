package com.scalaudio.amp.immutable.synth

import com.scalaudio.amp.immutable.control.AdsrEnvelope
import com.scalaudio.amp.immutable.ugen.SineStateGen
import com.scalaudio.core.engine.samplewise.AmpOutput
import com.scalaudio.core.engine.{Playback, Timeline}
import com.scalaudio.core.types.{AudioDuration, Pitch}
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.concurrent.duration._

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

    val o = AmpOutput(frameFunc, List(Playback()))
    Timeline.happen(5 seconds, List(o))
  }
}
