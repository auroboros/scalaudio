package scalaudio.core.midi

/**
  * Created by johnmcgill on 12/8/16.
  *
  * Adapted from JSyn MessageParser
  *
  */
trait ScalaudioMessageParser {
  def parse(message: Array[Byte]) {
    val status: Int = message(0)
    val command: Int = status & 0xF0
    val channel: Int = status & 0x0F
    command match {
      case MidiCommandConstants.NoteOn =>
        val velocity: Int = message(2)
        if (velocity == 0) noteOff(channel, message(1), velocity)
        else noteOn(channel, message(1), velocity)
      case MidiCommandConstants.NoteOff =>
        noteOff(channel, message(1), message(2))
      case MidiCommandConstants.ControlChange =>
        controlChange(channel, message(1), message(2))
      case MidiCommandConstants.PitchBend =>
        val bend: Int = ((message(2) & 0x007F) << 7) + (message(1) & 0x007F)
        pitchBend(channel, bend)
    }
  }

  def pitchBend(channel: Int, bend: Int) {
  }

  def controlChange(channel: Int, index: Int, value: Int) {
  }

  def noteOn(channel: Int, pitch: Int, velocity: Int) {
  }

  def noteOff(channel: Int, pitch: Int, velocity: Int) {
  }
}
