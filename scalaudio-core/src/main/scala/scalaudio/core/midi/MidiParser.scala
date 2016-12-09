package scalaudio.core.midi

/**
  * Created by johnmcgill on 6/1/16.
  *
  * adapted from Jsyn
  */
object MidiParser {

  def parse(message: Array[Byte]): MidiCommand = {
    val status: Byte = message(0)
    val command: Int = status & 240
    val channel: Int = status & 15
    command match {
      case MidiCommandConstants.NoteOff =>
        NoteOff(channel, message(1), message(2))
      case MidiCommandConstants.NoteOn =>
        val velocity: Byte = message(2)
        if (velocity == 0) {
          NoteOff(channel, message(1), velocity)
        } else {
          NoteOn(channel, message(1), velocity)
        }
      case MidiCommandConstants.ControlChange =>
        CtrlChange(channel, message(1), message(2))
      case MidiCommandConstants.PitchBend =>
        val bend: Int = ((message(2) & 127) << 7) + (message(1) & 127)
        PitchBend(channel, bend)
    }
  }
}
