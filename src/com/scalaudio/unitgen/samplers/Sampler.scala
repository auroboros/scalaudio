package com.scalaudio.unitgen.samplers

import com.scalaudio.AudioContext
import com.scalaudio.math._
import com.scalaudio.syntax.{ScalaudioSyntaxHelpers, UnitParams}
import com.scalaudio.unitgen.UnitGen

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 1/5/16.
  */
case class Sampler(val rawSamples : List[WavetableType])(implicit audioContext: AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  import SamplerUtils._

  val soundSamples : Map[String, (SoundSample, SampleState)] = (rawSamples.zipWithIndex map {case (x,i) => (i.toString, (wavetableMode2Sample(x, 10 seconds), new SampleState()))}).toMap

  val sample = soundSamples.head._2._1
  var state = SampleState()
  val playbackRate = 1
  val incrementRate : Double = playbackRate * (soundSamples.head._2._1.samplingFreq / audioContext.config.SamplingRate)
  var position : Double = 0
  internalBuffers = List.fill(sample.wavetable.size)(Array.fill(audioContext.config.FramesPerBuffer)(0))

  override def computeBuffer(params : Option[UnitParams] = None) =
    0 until audioContext.config.FramesPerBuffer foreach { s =>
      sample.wavetable.indices foreach { c =>
        internalBuffers(c)(s) = computeSample(c, position, 1)
      }
      position += incrementRate
    }

  def computeSample(channel : Int, sIndex : Double, clipIndex : Int) : Double = {
    if (sIndex > sample.wavetable.head.length - 1) state.reset
    if (state.active) interpolatedSample(channel, sIndex) else 0
  }

  def activateSoundSample(sampleId : String) =
    state.activate
//    soundSamples.get(sampleId) foreach (_._2.active = true)

  //TODO : De-dupe this (it's in WavetableGen as well)
  def interpolatedSample(channel : Int, position : Double) : Double = {
    val (ind1 : Int, ind2 : Int) = (position.floor.toInt, position.ceil.toInt % sample.wavetable.head.length)
    val interpAmount : Double = position % 1
    linearInterpolate(sample.wavetable(channel)(ind1), sample.wavetable(channel)(ind2), interpAmount)
  }
}

//TODO: incrementRate needs to be dynamically determined a la playbackRate * (sample.samplingFreq / audioContext.config.SamplingRate)
case class SampleState(var incrementRate : Double = 1, var active : Boolean = false, var position : Int = 0) {
  def reset = {
    active = false
    position = 0
  }

  def activate = {
    reset
    active = true
  }
}