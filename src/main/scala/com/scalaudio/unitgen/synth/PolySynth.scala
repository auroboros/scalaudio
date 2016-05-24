package com.scalaudio.unitgen.synth

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{AudioDuration, Pitch}
import com.scalaudio.unitgen.{OscillatorParams, SineGen, UnitGen, UnitOsc}

/**
  * Created by johnmcgill on 5/23/16.
  */
case class SimpleNote(pitch: Pitch, duration: AudioDuration)
case class RealTimeSynth(osc: UnitOsc) extends UnitGen {
  var pitch : Pitch = ???
  var noteEnvelope : Option[Int] = ???

  def valueAtSample(time: AudioDuration) : Double =
    if (idle) 0
    else ???

  def idle : Boolean = noteEnvelope.isEmpty

  // Updates internal buffer
  override def computeBuffer(params: Option[UnitParams]): Unit = ???
}

class PolySynth[T >: UnitOsc](nVoices: Int)(implicit val audioContext: AudioContext) extends UnitGen {
  val voices = List.fill(nVoices)(RealTimeSynth(SineGen()))

  def playNote(note: SimpleNote) : Unit = {
    // add simpleNote to first idle voice
    // Start on next buffer, remove after rounded up buffer
    voices.find(_.idle) foreach (???)
  }

  def findIdleVoice : Int = ???

  // Updates internal buffer
  override def computeBuffer(params: Option[UnitParams] = None): Unit =
    voices.filterNot(_.idle).map(rts => rts.outputBuffers())
}
