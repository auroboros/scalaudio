package com.scalaudio.core.midi

import javax.sound.midi.{MidiMessage, Receiver}

import scala.collection.mutable.ListBuffer

/**
  * Created by johnmcgill on 6/1/16.
  */
case class QueueingMidiReceiver(commandQueue: ListBuffer[MidiCommand]) extends Receiver {
  def close() {
    System.out.print("Closed.")
  }

  def send(message: MidiMessage, timeStamp: Long) {
    val bytes: Array[Byte] = message.getMessage
    commandQueue += MidiParser.parse(bytes)
  }
}
