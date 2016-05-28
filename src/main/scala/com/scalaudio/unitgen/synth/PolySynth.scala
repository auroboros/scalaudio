package com.scalaudio.unitgen.synth

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{AudioDuration, Pitch, ScalaudioSyntaxHelpers}
import com.scalaudio.types.MultichannelAudio
import com.scalaudio.unitgen.{SineGen, UnitGen, UnitOsc}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._

/**
  * Created by johnmcgill on 5/23/16.
  */

case class RealTimeSynth(osc: UnitOsc)(implicit val audioContext: AudioContext) extends ScalaudioSyntaxHelpers {
  var endTime: Option[AudioDuration] = None

  val release: AudioDuration = 200.milliseconds

  def valueAtSample(sampleOffset: Int) : Double = osc.outputBuffers().head(sampleOffset) * envValueAtSample

  private def envValueAtSample : Double = 1

  def scheduleEnd(currentTime: AudioDuration) : Unit = endTime = Some(currentTime + release)

  def finished(currentTime: AudioDuration) : Boolean = endTime.exists(_ > currentTime)
}

class PolySynth[T >: UnitOsc](nVoices: Int)(implicit val audioContext: AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  val emptyAudio: MultichannelAudio = List(Array.fill(audioContext.config.framesPerBuffer)(0))
  var voices: mutable.ArrayBuffer[Option[RealTimeSynth]] = ArrayBuffer.fill(nVoices)(None)

  def noteOn(pitch: Pitch): Option[Int] = {// return voiceId
    val freeVoiceIndex: Option[Int] = voices.zipWithIndex.collectFirst{case (None, x) => x}

    freeVoiceIndex.foreach(n => voices.update(n, Some(RealTimeSynth(SineGen(pitch)))))

    freeVoiceIndex
  }

  def noteOff(voiceId: Int) =
    voices(voiceId).foreach(synth => synth.scheduleEnd((audioContext.State.currentBuffer + 1).buffers)) // Schedule ramp-down starting at next whole buffer

  // Updates internal buffer
  override def computeBuffer(params: Option[UnitParams] = None): Unit = {
    0 until audioContext.config.framesPerBuffer foreach { i =>
      internalBuffers.head(i) = voices.map{
        case Some(rts) => rts.valueAtSample(i)
        case None => 0.0
      }.sum
    }
  }
}
