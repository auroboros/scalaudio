package com.scalaudio.amp.midi

import com.scalaudio.amp.immutable.envelope.AdsrEnvelope
import com.scalaudio.amp.immutable.synth.MonosynthStateGen
import com.scalaudio.core.midi.NoteOn
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 6/2/16.
  */
class MonosynthMidiReceiverSpec extends FlatSpec with Matchers with CoreSyntax {

  "MonosynthReceiver" should "add a single attack/decay for a note on" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    audioContext.State.currentSample = 7000

    val adsrTemplate = AdsrEnvelope(30.millis,
      .95,
      40.millis,
      .5,
      60.millis,
      15.millis)

    val midiReceiver = MonosynthMidiReceiver(adsrTemplate, 30.millis)

    val newState = midiReceiver.addCommandToState(MonosynthStateGen.decodeInitialState(), NoteOn(1, 22, 100))

    println(newState)
  }

}
