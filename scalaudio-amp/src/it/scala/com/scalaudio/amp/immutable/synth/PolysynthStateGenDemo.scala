package com.scalaudio.amp.immutable.synth

import com.scalaudio.amp.AmpSyntax
import com.scalaudio.amp.engine.FrameFuncAmpOutput
import com.scalaudio.amp.immutable.envelope.AdsrEnvelope
import com.scalaudio.amp.immutable.ugen.SineStateGen
import com.scalaudio.core.types.{AudioDuration, Pitch}
import com.scalaudio.core.{AudioContext, IntegrationTestHarness, ScalaudioConfig}

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.concurrent.duration._

/**
  * Created by johnmcgill on 5/30/16.
  */
class PolysynthStateGenDemo extends IntegrationTestHarness with AmpSyntax {

  "Polysynth" should "jam concurrent beefy sinewaves" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))
    val adsrTemplate = AdsrEnvelope(300.millis,
      .95,
      400.millis,
      .5,
      500.millis, //600
      115.millis) //15

    val notes: SortedMap[AudioDuration, List[(Pitch, AdsrEnvelope)]] =
      TreeMap(
        (1.second: AudioDuration) ->(440.Hz, adsrTemplate),
        (2.second: AudioDuration) ->(550.Hz, adsrTemplate),
        (3.second: AudioDuration) ->(660.Hz, adsrTemplate),
        (4.second: AudioDuration) -> List((880.Hz, adsrTemplate),
          (445.Hz, adsrTemplate)),
        (5.second: AudioDuration) ->(220.Hz, adsrTemplate),
        (7.second: AudioDuration) -> List((770.Hz, adsrTemplate),
          (330.Hz, adsrTemplate)),
        (8.second: AudioDuration) ->(660.Hz, adsrTemplate),
        (8200.millis: AudioDuration) ->(550.Hz, adsrTemplate),
        (8500.millis: AudioDuration) ->(660.Hz, adsrTemplate),
        (9.second: AudioDuration) ->(440.Hz, adsrTemplate)
      )

    var polysynthState = PolysynthState(0, notes, Nil)

    val frameFunc = () => {
      polysynthState = PolysynthStateGen.nextState(polysynthState, SineStateGen)
      List(polysynthState.sample * .2)
    }

    FrameFuncAmpOutput(frameFunc).play(11.seconds)
  }
}
