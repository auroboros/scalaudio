package com.scalaudio.core.syntax

/**
  * Created by johnmcgill on 1/20/16.
  */
case class Pitch(freqInHz : Double) {
  def toHz : Double = freqInHz
}

object Pitch {
  def midiNote2Pitch(note: Double) : Pitch =
    Pitch(440.0 * Math.pow(2.0, (note - 69) * (1.0 / 12.0)))
}

// Rich type enhancements (implicit conversions are done to these)
case class PitchRichInt(i : Int) {
  def Hz : Pitch = Pitch(i)
  def MidiPitch : Pitch = Pitch.midiNote2Pitch(i)
}

case class PitchRichDouble(d : Double) {
  def Hz : Pitch = Pitch(d)
  def MidiPitch : Pitch = Pitch.midiNote2Pitch(d)
}