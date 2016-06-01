package com.scalaudio.core.midi

import javax.sound.midi.{MidiMessage, Receiver}

import com.jsyn.midi.MessageParser

/**
  * Created by johnmcgill on 6/1/16.
  */
case class SimpleMidiReceiver(messageParser : MessageParser) extends Receiver {

  def close() {
    System.out.print("Closed.")
  }

  def send(message: MidiMessage, timeStamp: Long) {
    val bytes: Array[Byte] = message.getMessage
    messageParser.parse(bytes)
  }
}
