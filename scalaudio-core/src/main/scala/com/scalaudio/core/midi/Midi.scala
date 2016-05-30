package com.scalaudio.core.midi

import javax.sound.midi.{MidiDevice, MidiMessage, Receiver}

import com.jsyn.devices.javasound.MidiDeviceTools
import com.jsyn.midi.{MessageParser, MidiConstants}

/**
  * Created by johnmcgill on 5/30/16.
  *
  * Bunch of stuff adapted from JSyn
  */
class CustomReceiver extends Receiver {
  val messageParser = new MyParser()

  def close() {
    System.out.print("Closed.")
  }

  def send(message: MidiMessage, timeStamp: Long) {
    val bytes: Array[Byte] = message.getMessage
    messageParser.parse(bytes)
  }
}

class MyParser extends MessageParser {
  override def controlChange(channel: Int, index: Int, value: Int) {
    if (index == 1) {
      //      vibratoDepth = 0.1 * value / 128.0
      //      lfo.amplitude.set(vibratoDepth)
    }
    else if (index == 102) {
      val bump: Double = 0.95
      if (value < 64) {
        //        vibratoRate *= bump
      }
      else {
        //        vibratoRate *= 1.0 / bump
      }
      //      System.out.println("vibratoRate = " + vibratoRate)
      //      lfo.frequency.set(vibratoRate)
    }
  }

  override def noteOff(channel: Int, noteNumber: Int, velocity: Int) {
    //    allocator.noteOff(noteNumber, synth.createTimeStamp)
  }

  override def noteOn(channel: Int, noteNumber: Int, velocity: Int) {
    //    val frequency: Double = convertPitchToFrequency(noteNumber)
    //    val amplitude: Double = velocity / (4 * 128.0)
    //    val timeStamp: TimeStamp = synth.createTimeStamp
    //    allocator.noteOn(noteNumber, frequency, amplitude, timeStamp)
  }

  override def pitchBend(channel: Int, bend: Int) {
    val fraction: Double = (bend - MidiConstants.PITCH_BEND_CENTER) / MidiConstants.PITCH_BEND_CENTER.toDouble
    System.out.println("bend = " + bend + ", fraction = " + fraction)
  }
}

object Midi {

  def connectKeyboard(): Unit = {
    val keyboard: MidiDevice = MidiDeviceTools.findKeyboard
    val receiver: Receiver = new CustomReceiver
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
