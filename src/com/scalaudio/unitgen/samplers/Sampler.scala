package com.scalaudio.unitgen.samplers

import com.scalaudio.AudioContext
import com.scalaudio.engine.Playback
import com.scalaudio.math._
import com.scalaudio.syntax.{AudioDuration, ScalaudioSyntaxHelpers, UnitParams}
import com.scalaudio.unitgen.UnitGen

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 1/5/16.
  */
case class Sampler(val sampleDeck : List[SampleDeck])(implicit audioContext: AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  import SamplerUtils._

  val soundSamples : Map[String, SamplerTape] = (sampleDeck.zipWithIndex map {case (deck,i) => (deck.label, SamplerTape(wavetableMode2Sample(deck.wtType, 10 seconds), new SampleState(), deck.triggerTimes))}).toMap

  val sampleTape = soundSamples.head._2
  internalBuffers = List.fill(sampleTape.soundSample.wavetable.size)(Array.fill(audioContext.config.FramesPerBuffer)(0))

  override def computeBuffer(params : Option[UnitParams] = None) =
    0 until audioContext.config.FramesPerBuffer foreach { s =>
      triggerCheck((currentFrame buffers) + AudioDuration(s))
      sampleTape.soundSample.wavetable.indices foreach { c =>
        internalBuffers(c)(s) = computeSample(c, sampleTape.state.position, 1)
      }
      sampleTape.state.position += sampleTape.incrementRate
    }

  def computeSample(channel : Int, sIndex : Double, clipIndex : Int) : Double = {
    if (sIndex > sampleTape.soundSample.wavetable.head.length - 1) sampleTape.state.reset()
    if (sampleTape.state.active) interpolatedSample(channel, sIndex) else 0
  }

  def triggerCheck(currentTime : AudioDuration) =
    soundSamples.values.filter(_.triggerTimes.exists(_ == currentTime)).foreach(_.state.activate())

  def activateSoundSample(sampleId : String) =
    soundSamples.get(sampleId) foreach (_.state.activate())

  //TODO : De-dupe this (it's in WavetableGen as well)
  def interpolatedSample(channel : Int, position : Double) : Double = {
    val (ind1 : Int, ind2 : Int) = (position.floor.toInt, position.ceil.toInt % sampleTape.soundSample.wavetable.head.length)
    val interpAmount : Double = position % 1
    linearInterpolate(sampleTape.soundSample.wavetable(channel)(ind1), sampleTape.soundSample.wavetable(channel)(ind2), interpAmount)
  }
}

case class SampleDeck(label : String, wtType : WavetableType, triggerTimes : Option[List[AudioDuration]] = None)

case class SamplerTape(soundSample: SoundSample, var state : SampleState, triggerTimes : Option[List[AudioDuration]])(implicit audioContext: AudioContext) {
  val incrementRate : Double = state.playbackRate * (soundSample.samplingFreq / audioContext.config.SamplingRate)
}

//TODO: incrementRate needs to be dynamically determined a la playbackRate * (sample.samplingFreq / audioContext.config.SamplingRate)
case class SampleState(var playbackRate : Double = 1, var active : Boolean = false, var position : Double = 0) {
  def reset() = {
    active = false
    position = 0
  }

  def activate() = {
    reset()
    active = true
  }
}