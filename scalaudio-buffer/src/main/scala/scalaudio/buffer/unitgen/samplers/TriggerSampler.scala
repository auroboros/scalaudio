package scalaudio.buffer.unitgen.samplers

import scala.concurrent.duration._
import scalaudio.buffer.unitgen.UnitGen
import scalaudio.core.types.AudioDuration
import scalaudio.core.{AudioContext, CoreSyntax}
import scalaudio.core.math._
import scalaudio.units.sampler.{SoundSample, WavetableType}
/**
  * Created by johnmcgill on 1/5/16.
  */
case class TriggerSampler(wtType : WavetableType, triggerTimes : List[AudioDuration] = Nil)(implicit audioContext: AudioContext) extends UnitGen with CoreSyntax {
  import scalaudio.units.sampler.SamplerUtils._

  val sampleTape : SamplerTape = SamplerTape(wavetableMode2Sample(wtType, 10.seconds), new SampleState(), triggerTimes)

  internalBuffers = List.fill(sampleTape.soundSample.wavetable.size)(Array.fill(audioContext.config.framesPerBuffer)(0))

  override def computeBuffer(params : Option[UnitParams] = None) =
    0 until audioContext.config.framesPerBuffer foreach { s =>
      triggerCheck(currentFrame.buffers + AudioDuration(s))
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
    if (sampleTape.triggerTimes.contains(currentTime)) activateSoundSample()

  def activateSoundSample() =
    sampleTape.state.activate()

  //TODO : De-dupe this (it's in WavetableGen as well)
  def interpolatedSample(channel : Int, position : Double) : Double = {
    val (ind1 : Int, ind2 : Int) = (position.floor.toInt, position.ceil.toInt % sampleTape.soundSample.wavetable.head.length)
    val interpAmount : Double = position % 1
    linearInterpolate(sampleTape.soundSample.wavetable(channel)(ind1), sampleTape.soundSample.wavetable(channel)(ind2), interpAmount)
  }
}

case class SamplerTape(soundSample: SoundSample, var state : SampleState, triggerTimes : List[AudioDuration])(implicit audioContext: AudioContext) {
  val incrementRate : Double = state.playbackRate * (soundSample.samplingFreq / audioContext.config.samplingRate)
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