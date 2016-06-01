package com.scalaudio.core.midi

import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 6/1/16.
  */
class MidiConnectorSpec extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "MidiConnector" should "attach some MIDI input to the dummy parser" in {
    MidiConnector.connectKeyboard(SimpleMidiReceiver(PrintlnMidiParser()))

    Thread.sleep(10000)
  }
}