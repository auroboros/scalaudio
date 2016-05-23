package com.scalaudio.unitgen.samplers

import com.scalaudio.AudioContext
import com.scalaudio.math._
import com.scalaudio.syntax.{AudioDuration, ScalaudioSyntaxHelpers}
import com.scalaudio.unitgen.UnitGen

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 1/24/16.
  */
case class WavetableGen(initMode : WavetableType, playbackRate : Double = 1)(implicit audioContext : AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...
  import SamplerUtils._

  // Determines resolution, essentially (lower = more interpolation, higher = bigger buffer)
  val periodLength : AudioDuration = 10.seconds

  val sample : SoundSample = wavetableMode2Sample(initMode, periodLength)

//  def scaledLength : AudioDuration = AudioDuration((sample.wavetable.head.length / (sample.samplingFreq / audioContext.config.SamplingRate) / playbackRate).toLong)

  val incrementRate : Double = playbackRate * (sample.samplingFreq / audioContext.config.SamplingRate)

  var position : Double = 0
  internalBuffers = List.fill(sample.wavetable.size)(Array.fill(audioContext.config.FramesPerBuffer)(0))

  // Updates internal buffer
  override def computeBuffer(params : Option[UnitParams] = None) : Unit = {
    sample.wavetable.indices foreach {c =>
      0 until audioContext.config.FramesPerBuffer foreach {s => internalBuffers(c)(s) = interpolatedSample(c,(position + s * incrementRate) % sample.wavetable.head.length)}
    }
    position = (position + audioContext.config.FramesPerBuffer * incrementRate) % sample.wavetable.head.length
  }

  def interpolatedSample(channel : Int, position : Double) : Double = {
    val (ind1 : Int, ind2 : Int) = (position.floor.toInt, position.ceil.toInt % sample.wavetable.head.length)
    val interpAmount : Double = position % 1
    linearInterpolate(sample.wavetable(channel)(ind1), sample.wavetable(channel)(ind2), interpAmount)
  }
}