package com.scalaudio.unitgen.synth

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{AudioDuration, Pitch, ScalaudioSyntaxHelpers}
import com.scalaudio.types.MultichannelAudio
import com.scalaudio.unitgen.{OscillatorParams, SineGen, UnitGen, UnitOsc}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by johnmcgill on 5/23/16.
  */
case class SimpleNote(pitch: Pitch, duration: AudioDuration)

case class RealTimeSynth(osc: UnitOsc) extends UnitGen {
  var note: Option[SimpleNote] = None

  def idle: Boolean = note.isEmpty

  def attemptEviction() = if (???) note = None

  def bufferEnv = ???

  // Updates internal buffer
  override def computeBuffer(params: Option[UnitParams]): Unit = ???
}

class PolySynth[T >: UnitOsc](nVoices: Int)(implicit val audioContext: AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  val emptyAudio: MultichannelAudio = List(Array.fill(audioContext.config.framesPerBuffer)(0))
  val voices = List.fill(nVoices)(RealTimeSynth(SineGen()))
  var noteQueue: ListBuffer[SimpleNote] = new ListBuffer[SimpleNote]()

  def playNote(note: SimpleNote): Unit =
    if (noteQueue.size <= nVoices) noteQueue += note //Can't be more idle than max voices anyway so might as well limit size...

  // Updates internal buffer
  override def computeBuffer(params: Option[UnitParams] = None): Unit = {
    val idleVoices = voices.filter(_.idle)
    (idleVoices zip noteQueue).foreach { case (rts, sn) => rts.note = Some(sn) }
    noteQueue.clear() // If there aren't enough voices, toss the rest of the notes out...

    val voicesInUse = voices.filterNot(_.idle)

    internalBuffers = if (voicesInUse.isEmpty) emptyAudio
    else {
      val allVoiceAudio: List[MultichannelAudio] = voicesInUse.map(rts => rts.outputBuffers())
      allVoiceAudio.tail.foldLeft(allVoiceAudio.head)((accum, current) => accum mix current) // TODO: Make more efficient summer
    }

    voicesInUse.foreach(_.attemptEviction())
  }
}
