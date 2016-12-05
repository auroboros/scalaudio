package scalaudio.units.sampler

import signalz.SequentialState

import scalaudio.core.math._
import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.core.{AudioContext, CoreSyntax}
import scala.concurrent.duration._

/**
  * Created by johnmcgill on 12/5/16.
  */
case class TriggerSamplerState(frame: Frame,
                               playbackRate: Double = 1,
                               position: Double = 0,
                               trigger: Boolean)

case class TriggerSampler(wtType: WavetableType,
                          triggerTimes: List[AudioDuration] = Nil)(implicit audioContext: AudioContext)
  extends SequentialState[TriggerSamplerState, AudioContext]
    with CoreSyntax {

  import SamplerUtils._

  val soundSample: SoundSample = wavetableMode2Sample(wtType, 10.seconds)

  def nextState(state: TriggerSamplerState)(implicit audioContext: AudioContext) = ???
//  {
//    val incrementRate: Double = state.playbackRate * (soundSample.samplingFreq / audioContext.config.samplingRate)
//    soundSample.wavetable.indices foreach { c =>
//      computeSample(c, state.position, 1)
//    }
//    state.position += incrementRate
//  }

  def computeSample(channel: Int, sIndex: Double, clipIndex: Int): Double = {
    interpolatedSample(channel, sIndex)
  }

  //TODO : De-dupe this (it's in WavetableGen as well)
  def interpolatedSample(channel: Int, position: Double): Double = {
    val (ind1: Int, ind2: Int) = (position.floor.toInt, position.ceil.toInt % soundSample.wavetable.head.length)
    val interpAmount: Double = position % 1
    linearInterpolate(soundSample.wavetable(channel)(ind1), soundSample.wavetable(channel)(ind2), interpAmount)
  }
}