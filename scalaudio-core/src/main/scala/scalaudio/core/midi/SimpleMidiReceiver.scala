package scalaudio.core.midi

import javax.sound.midi.{MidiMessage, Receiver}

/**
  * Created by johnmcgill on 6/1/16.
  */
case class SimpleMidiReceiver(messageParser : ScalaudioMessageParser) extends Receiver {

  def close() {
    System.out.print("Closed.")
  }

  def send(message: MidiMessage, timeStamp: Long) {
    val bytes: Array[Byte] = message.getMessage
    messageParser.parse(bytes)
  }
}
