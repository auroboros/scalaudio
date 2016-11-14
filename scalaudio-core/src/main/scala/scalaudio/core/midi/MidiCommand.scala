package com.scalaudio.core.midi

/**
  * Created by johnmcgill on 6/1/16.
  */
sealed trait MidiCommand
case class PitchBend(channel: Int, bend: Int) extends MidiCommand
case class CtrlChange(channel: Int, index: Int, value: Int) extends MidiCommand
case class NoteOn(channel: Int, noteNumber: Int, velocity: Int) extends MidiCommand
case class NoteOff(channel: Int, noteNumber: Int, velocity: Int) extends MidiCommand