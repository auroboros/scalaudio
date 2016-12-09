package scalaudio.core.midi

/**
  * Created by johnmcgill on 6/1/16.
  */
sealed trait MidiCommand
case class PitchBend(channel: Int, bend: Int) extends MidiCommand {
  import MidiCommandConstants.PitchBendCenter
  val fraction: Double = (bend - PitchBendCenter) / PitchBendCenter.toDouble
}
case class CtrlChange(channel: Int, index: Int, value: Int) extends MidiCommand
case class NoteOn(channel: Int, noteNumber: Int, velocity: Int) extends MidiCommand
case class NoteOff(channel: Int, noteNumber: Int, velocity: Int) extends MidiCommand

object MidiCommandConstants {

  // See "MidiConstants" in JSyn for this + more
  val PitchBendCenter = 8192
  val NoteOff: Int = 128
  val NoteOn: Int = 144
  val POLYPHONIC_AFTERTOUCH: Int = 160
  val ControlChange: Int = 176
  val PROGRAM_CHANGE: Int = 192
  val CHANNEL_AFTERTOUCH: Int = 208
  val PitchBend: Int = 224
  val SYSTEM_COMMON: Int = 240
  val PITCH_BEND_CENTER: Int = 8192
  val PITCH_NAMES: Array[String] = Array[String]("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
  private[midi] val CONCERT_A_FREQUENCY: Double = 440.0D
  private[midi] val CONCERT_A_PITCH: Double = 69.0D
}