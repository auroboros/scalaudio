package com.scalaudio.syntax

import com.scalaudio.Config

import scala.concurrent.duration._
import scala.concurrent.duration.FiniteDuration

/**
  * Created by johnmcgill on 1/18/16.
  */
// TODO: allow this to accept some form of samples as input instead since this is really the smallest indivisible unit?
case class AudioDuration(finiteDuration: FiniteDuration) {
  def toSamples : Long = (finiteDuration.toNanos * Config.SamplingRate / Math.pow(10,9)).toLong

  // TODO: Terminology here (& throughout codebase) is wrong... This isn't really a frame, its a buffer of frames...
  def toBufferConsumptions : Int = (toSamples.toDouble / Config.FramesPerBuffer.toDouble).ceil.toInt
}

object DurationConverter {
  def buffersToFiniteDuration(length: Int): FiniteDuration = samplesToFiniteDuration(length * Config.FramesPerBuffer)

  def samplesToFiniteDuration(nSamples : Int) = (nSamples.toDouble / Config.SamplingRate) seconds
}

case class AudioDurationRichInt(val n : Int) {
  import DurationConverter._

  def samples = AudioDuration(samplesToFiniteDuration(n))

  def buffers = AudioDuration(buffersToFiniteDuration(n))
}