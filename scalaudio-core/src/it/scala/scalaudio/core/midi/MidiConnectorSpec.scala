package scalaudio.core.midi

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.CoreSyntax

/**
  * Created by johnmcgill on 6/1/16.
  */
class MidiConnectorSpec extends FlatSpec with Matchers with CoreSyntax {
  "MidiConnector" should "attach some MIDI input to the dummy parser" in {
    MidiConnector.connectKeyboard(SimpleMidiReceiver(PrintlnMidiParser()))

    Thread.sleep(10000)
  }
}