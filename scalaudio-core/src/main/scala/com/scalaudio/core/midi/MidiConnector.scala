package com.scalaudio.core.midi

import javax.sound.midi.{MidiDevice, MidiMessage, Receiver}

import com.jsyn.devices.javasound.MidiDeviceTools
import com.jsyn.midi.{MessageParser, MidiConstants}

/**
  * Created by johnmcgill on 5/30/16.
  *
  * Bunch of stuff adapted from JSyn
  */

object MidiConnector {

  def connectKeyboard(messageParser: MessageParser): Unit = {
    val keyboard: MidiDevice = MidiDeviceTools.findKeyboard
    val receiver: Receiver = SimpleMidiReceiver(messageParser)

    // Just use default synthesizer.
    if (keyboard != null) {
      keyboard.open()
      keyboard.getTransmitter.setReceiver(receiver)
      System.out.println("Play MIDI keyboard: " + keyboard.getDeviceInfo.getDescription)
    }
    else {
      System.out.println("Could not find a keyboard.")
    }
  }
}
