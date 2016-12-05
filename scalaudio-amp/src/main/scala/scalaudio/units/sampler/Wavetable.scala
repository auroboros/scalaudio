package scalaudio.units.sampler

import signalz.SequentialState

import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.core.{AudioContext, CoreSyntax}
import scala.concurrent.duration._
import scalaudio.core.math.linearInterpolate

/**
  * Created by johnmcgill on 12/5/16.
  */
case class WavetableState(
                           frame: Frame,
                           position: Double
                         )

case class Wavetable(initMode: WavetableType,
                     playbackRate: Double = 1)(implicit audioContext: AudioContext)
  extends SequentialState[WavetableState, AudioContext] with CoreSyntax {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...

  import SamplerUtils._

  // TODO: Make these util objects into traits so they can just be mixed in? More obvious or less?

  // Determines resolution, essentially (lower = more interpolation, higher = bigger buffer)
  val periodLength: AudioDuration = 10.seconds

  val sample: SoundSample = wavetableMode2Sample(initMode, periodLength)

  //  def scaledLength : AudioDuration = AudioDuration((sample.wavetable.head.length / (sample.samplingFreq / audioContext.config.SamplingRate) / playbackRate).toLong)

  val incrementRate: Double = playbackRate * (sample.samplingFreq / audioContext.config.samplingRate)

  def nextState(state: WavetableState)(implicit audioContext: AudioContext) =
    state.copy(
      frame = (sample.wavetable.indices map { c =>
        interpolatedSample(c, state.position % sample.wavetable.head.length)
      }).toArray,
      position = (state.position + incrementRate) % sample.wavetable.head.length
    )

  def interpolatedSample(channel: Int, position: Double): Double = {
    val (ind1: Int, ind2: Int) = (position.floor.toInt, position.ceil.toInt % sample.wavetable.head.length)
    val interpAmount: Double = position % 1
    linearInterpolate(sample.wavetable(channel)(ind1), sample.wavetable(channel)(ind2), interpAmount)
  }
}
