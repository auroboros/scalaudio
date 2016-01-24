package com.scalaudio.syntax

import com.scalaudio.AudioContext

import scala.concurrent.duration._
import scala.concurrent.duration.FiniteDuration

/**
  * Created by johnmcgill on 1/18/16.
  */
// TODO: allow this to accept some form of samples as input instead since this is really the smallest indivisible unit?
case class AudioDuration(val nSamples : Long)(implicit audioContext: AudioContext) extends Ordered[AudioDuration] {
  import DurationConverter._

  def toSamples : Long = nSamples

  // TODO: Terminology here (& throughout codebase) is wrong... This isn't really a frame, its a buffer of frames...
  def toBuffers : Long = samples2Buffers(nSamples)

  def +(other : AudioDuration) : AudioDuration = AudioDuration(nSamples + other.nSamples)

  def -(other : AudioDuration) : AudioDuration = AudioDuration(nSamples - other.nSamples)

  override def compare(that: AudioDuration) : Int = Math.signum(nSamples - that.nSamples).toInt
}

object DurationConverter {
  def finiteDuration2Samples(duration : FiniteDuration)(implicit audioContext: AudioContext) : Long =
    (duration.toNanos * audioContext.config.SamplingRate / Math.pow(10,9)).toLong

  def samples2Buffers(nSamples : Long)(implicit audioContext: AudioContext) : Long = (nSamples.toDouble / audioContext.config.FramesPerBuffer.toDouble).ceil.toLong

  def buffers2Samples(nBuffers : Long)(implicit audioContext: AudioContext) : Long = nBuffers * audioContext.config.FramesPerBuffer

  def buffers2FiniteDuration(length: Int)(implicit audioContext: AudioContext) : FiniteDuration =
    samples2FiniteDuration(length * audioContext.config.FramesPerBuffer)

  def samples2FiniteDuration(nSamples : Int)(implicit audioContext: AudioContext) =
    (nSamples.toDouble / audioContext.config.SamplingRate) seconds
}

case class AudioDurationRichInt(val n : Int)(implicit audioContext: AudioContext) {
  import DurationConverter._

  def samples = AudioDuration(n)

  def buffers = AudioDuration(buffers2Samples(n))
}