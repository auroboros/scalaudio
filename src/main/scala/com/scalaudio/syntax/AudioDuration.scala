package com.scalaudio.syntax

import com.scalaudio.AudioContext

import scala.concurrent.duration._
import scala.concurrent.duration.FiniteDuration

/**
  * Created by johnmcgill on 1/18/16.
  */
// TODO: allow this to accept some form of samples as input instead since this is really the smallest indivisible unit?
case class AudioDuration(nSamples : Long)(implicit audioContext: AudioContext) extends Ordered[AudioDuration] {
  import DurationConverter._

  def toSamples : Long = nSamples

  // TODO: Terminology here (& throughout codebase) is wrong... This isn't really a frame, its a buffer of frames...
  def toBuffers : Long = samples2Buffers(nSamples) // TODO: Should be Double?? Buffer is not smallest unit in system

  def toBeats : Double = samples2Beats(nSamples)

  // TODO: Should include conversion back to finite dur, for convenience

  def +(other : AudioDuration) : AudioDuration = AudioDuration(nSamples + other.nSamples)

  def -(other : AudioDuration) : AudioDuration = AudioDuration(nSamples - other.nSamples)

  override def compare(that: AudioDuration) : Int = Math.signum(nSamples - that.nSamples).toInt
}

object DurationConverter {
  def finiteDuration2Samples(duration : FiniteDuration)(implicit audioContext: AudioContext) : Long =
    (duration.toNanos * audioContext.config.SamplingRate / Math.pow(10,9)).toLong

  def samples2Buffers(nSamples : Long)(implicit audioContext: AudioContext) : Long = (nSamples.toDouble / audioContext.config.FramesPerBuffer.toDouble).ceil.toLong

  def buffers2Samples(nBuffers : Long)(implicit audioContext: AudioContext) : Long = nBuffers * audioContext.config.FramesPerBuffer

  def beats2Samples(nBeats : Long)(implicit audioContext: AudioContext) : Long = (nBeats * finiteDuration2Samples(1 minute) / audioContext.config.BeatsPerMinute).toLong

  def measures2Samples(nMeasures : Long)(implicit audioContext: AudioContext) : Long = (beats2Samples(nMeasures) * audioContext.config.BeatsPerMeasure).toLong

  def samples2Beats(nSamples : Long)(implicit audioContext: AudioContext) : Double = ???

  def buffers2FiniteDuration(length: Int)(implicit audioContext: AudioContext) : FiniteDuration =
    samples2FiniteDuration(length * audioContext.config.FramesPerBuffer)

  def samples2FiniteDuration(nSamples : Int)(implicit audioContext: AudioContext) =
    (nSamples.toDouble / audioContext.config.SamplingRate) seconds
}

case class AudioDurationRichInt(n : Int)(implicit audioContext: AudioContext) {
  import DurationConverter._

  def samples = AudioDuration(n)

  def buffers = AudioDuration(buffers2Samples(n))

  def beats = AudioDuration(beats2Samples(n))

  def measures = AudioDuration(measures2Samples(n))

//  def measure&beat = AudioDuration(beats2Samples(n)) // Should be an implicit on a tuple?
} //TODO: should be able to give these as Doubles as well