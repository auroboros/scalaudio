package scalaudio.amp.midi

import com.scalaudio.core.midi.MidiConnector

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
import scalaudio.amp.immutable.control.AdsrEnvelope
import scalaudio.amp.immutable.synth.MonosynthStateGen
import scalaudio.amp.immutable.ugen.SineStateGen
import scalaudio.core.engine.samplewise.AmpOutput
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

    var monosynthState = MonosynthStateGen.decodeInitialState(TreeMap.empty[AudioDuration, (Pitch, AdsrEnvelope)])

    val midiReceiver = MonosynthMidiReceiver(adsrTemplate, 150.millis)
    MidiConnector.connectKeyboard(midiReceiver)

    val frameFunc = () => {
      monosynthState = MonosynthStateGen.nextState(
        midiReceiver.processMidiCommandsIntoState(monosynthState),
        SineStateGen
      )
      Array(monosynthState.sample)
    }

    AmpOutput(frameFunc).play(5 hours)
  }
}
