package scalaudio.amp.midi

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
import scalaudio.units.control.AdsrEnvelope
import scalaudio.units.synth.{Monosynth, MonosynthMidiReceiver}
import scalaudio.units.ugen.Sine
import scalaudio.core.engine.StreamCollector
import scalaudio.core.midi.MidiConnector
import scalaudio.core.types.{AudioDuration, Pitch}
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 6/2/16.
  */
class MonosynthMidiReceiverDemo extends ScalaudioCoreTestHarness {

  "Monosynth Midi receiver" should "play sines in real-time" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))
    val adsrTemplate = AdsrEnvelope(10.millis,
      .95,
      40.millis,
      .5,
      60.millis,
      115.millis)

    var monosynthState = Monosynth.decodeInitialState(TreeMap.empty[AudioDuration, (Pitch, AdsrEnvelope)])

    val midiReceiver = MonosynthMidiReceiver(adsrTemplate, 150.millis)
    MidiConnector.connectKeyboard(midiReceiver)

    val frameFunc = () => {
      monosynthState = Monosynth(Sine).nextState(
        midiReceiver.processMidiCommandsIntoState(monosynthState)
      )
      Array(monosynthState.sample)
    }

    StreamCollector(frameFunc).play(5 hours)
  }
}
