package scalaudio.units.sampler

import signalz.SequentialState

import scalaudio.core.math._
import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.core.{AudioContext, CoreSyntax}
import scala.concurrent.duration._

/**
  * Created by johnmcgill on 12/5/16.
  */
case class TriggerSamplerState(frame: Frame = Array.empty,
                               playbackRate: Double = 1,
                               playbackPositions: List[Double] = Nil
                              )

case class TriggerSampler(wtType: WavetableType)(implicit audioContext: AudioContext)
  extends SequentialState[TriggerSamplerState, AudioContext]
    with CoreSyntax {

  import SamplerUtils._

  val soundSample: SoundSample = wavetableMode2Sample(wtType, 10.seconds)

  def nextState(state: TriggerSamplerState)(implicit audioContext: AudioContext) = {
    val incrementRate: Double = state.playbackRate * (soundSample.samplingFreq / audioContext.config.samplingRate)
    val frames = state.playbackPositions.map { pos =>
      soundSample.wavetable.indices.toArray map { c =>
        soundSample.interpolatedSample(c, pos)
      } // TODO: Interpolated frame moved into SoundSample
    }
    val outFrame = soundSample.wavetable.indices.toArray map {c =>
      frames.map(_(c)).sum
    }
    state.copy(
      frame = outFrame,
      playbackPositions = state.playbackPositions.map(_ + incrementRate)
        .filter(_ <= soundSample.wavetable.head.length)
    )
  }
}