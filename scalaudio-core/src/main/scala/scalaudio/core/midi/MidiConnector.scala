package scalaudio.core.midi

import javax.sound.midi.{MidiDevice, Receiver}

import com.jsyn.devices.javasound.MidiDeviceTools

/**
  * Created by johnmcgill on 5/30/16.
  *
  * Bunch of stuff adapted from JSyn
  */

object MidiConnector {

  def connectKeyboard(receiver: Receiver): Unit = {
    val keyboard: MidiDevice = MidiDeviceTools.findKeyboard

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
