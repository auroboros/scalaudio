package scalaudio.core.midi

/**
  * Created by johnmcgill on 6/1/16.
  */
case class PrintlnMidiParser() extends ScalaudioMessageParser {
  override def controlChange(channel: Int, index: Int, value: Int) {
    println("ctrl change")
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
    println("note off")
    //    allocator.noteOff(noteNumber, synth.createTimeStamp)
  }

  override def noteOn(channel: Int, noteNumber: Int, velocity: Int) {
    println("note on")
    //    val frequency: Double = convertPitchToFrequency(noteNumber)
    //    val amplitude: Double = velocity / (4 * 128.0)
    //    val timeStamp: TimeStamp = synth.createTimeStamp
    //    allocator.noteOn(noteNumber, frequency, amplitude, timeStamp)
  }

  override def pitchBend(channel: Int, bend: Int) {
    println("pitch bend")
    val fraction: Double = (bend - MidiCommandConstants.PITCH_BEND_CENTER) / MidiCommandConstants.PITCH_BEND_CENTER.toDouble
    System.out.println("bend = " + bend + ", fraction = " + fraction)
  }
}
