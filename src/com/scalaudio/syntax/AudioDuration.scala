package com.scalaudio.syntax

import com.scalaudio.Config

import scala.concurrent.duration._
import scala.concurrent.duration.FiniteDuration

/**
  * Created by johnmcgill on 1/18/16.
  */
// TODO: allow this to accept some form of samples as input instead since this is really the smallest indivisible unit?
case class AudioDuration(val nSamples : Long) extends Ordered[AudioDuration] {
  import DurationConverter._

  def toSamples : Long = nSamples

  // TODO: Terminology here (& throughout codebase) is wrong... This isn't really a frame, its a buffer of frames...
  def toBuffers : Long = samples2Buffers(nSamples)

  def +(other : AudioDuration) : AudioDuration = AudioDuration(nSamples + other.nSamples)

  def -(other : AudioDuration) : AudioDuration = AudioDuration(nSamples - other.nSamples)

  override def compare(that: AudioDuration): Int = Math.signum(nSamples - that.nSamples).toInt
}

object DurationConverter {
  def finiteDuration2Samples(duration : FiniteDuration) : Long = (duration.toNanos * Config.SamplingRate / Math.pow(10,9)).toLong

  def samples2Buffers(nSamples : Long) : Long = (nSamples.toDouble / Config.FramesPerBuffer.toDouble).ceil.toLong

  def buffers2Samples(nBuffers : Long) : Long = nBuffers * Config.FramesPerBuffer

  def buffers2FiniteDuration(length: Int): FiniteDuration = samples2FiniteDuration(length * Config.FramesPerBuffer)

  def samples2FiniteDuration(nSamples : Int) = (nSamples.toDouble / Config.SamplingRate) seconds
}

case class AudioDurationRichInt(val n : Int) {
  import DurationConverter._

  def samples = AudioDuration(n)

  def buffers = AudioDuration(buffers2Samples(n))
}