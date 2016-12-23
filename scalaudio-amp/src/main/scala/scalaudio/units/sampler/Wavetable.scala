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

case class Wavetable(waveTableType: WavetableType,
                     playbackRate: Double = 1)(implicit audioContext: AudioContext)
  extends SequentialState[WavetableState, AudioContext] with CoreSyntax {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...

  import SamplerUtils._

  // TODO: Make these util objects into traits so they can just be mixed in? More obvious or less?

  // Determines resolution, essentially (lower = more interpolation, higher = bigger buffer)
  val periodLength: AudioDuration = 10.seconds

  val soundSample: SoundSample = wavetableMode2Sample(waveTableType, periodLength)

  //  def scaledLength : AudioDuration = AudioDuration((sample.wavetable.head.length / (sample.samplingFreq / audioContext.config.SamplingRate) / playbackRate).toLong)

  val incrementRate: Double = playbackRate * (soundSample.samplingFreq / audioContext.config.samplingRate)

  def nextState(state: WavetableState)(implicit audioContext: AudioContext) =
    state.copy(
      frame = (soundSample.wavetable.indices map { c =>
        soundSample.interpolatedSample(c, state.position % soundSample.fileLengthInSamples)
      }).toArray,
      position = (state.position + incrementRate) % soundSample.fileLengthInSamples
    )
}
