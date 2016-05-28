package com.scalaudio.core.syntax

/**
  * Created by johnmcgill on 1/20/16.
  */
case class Pitch(val freqInHz : Double) {
  def toHz : Double = freqInHz
}

// Rich type enhancements (implicit conversions are done to these)
case class PitchRichInt(val i : Int) {
  def Hz : Pitch = Pitch(i)
}

case class PitchRichDouble(val d : Double) {
  def Hz : Pitch = Pitch(d)
}