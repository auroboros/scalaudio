package scalaudio.core.midi

import javax.sound.midi._

/**
  * Created by johnmcgill on 5/30/16.
  *
  * Bunch of stuff adapted from JSyn
  */

object MidiConnector {

  def connectKeyboard(receiver: Receiver): Unit = {
    val keyboard: MidiDevice = findDefaultKeyboard

    // Just use default synthesizer.
    if (keyboard != null) {
      keyboard.open()
      keyboard.getTransmitter.setReceiver(receiver)
      println("Play MIDI keyboard: " + keyboard.getDeviceInfo.getDescription)
    }
    else {
      println("Could not find a keyboard.")
    }
  }

  def findDefaultKeyboard: MidiDevice = {
    val maybeChosenDeviceInfo = MidiSystem.getMidiDeviceInfo.find{ deviceInfo =>
      val device = MidiSystem.getMidiDevice(deviceInfo)
      !device.isInstanceOf[Synthesizer] && !device.isInstanceOf[Sequencer] && device.getMaxTransmitters != 0  // deviceInfo.getDescription.toLowerCase.contains(var0.toLowerCase)
    }

    val chosenDeviceInfo = maybeChosenDeviceInfo.get

    println(s"chose ${chosenDeviceInfo.getDescription}")
    MidiSystem.getMidiDevice(chosenDeviceInfo)
  }
}
