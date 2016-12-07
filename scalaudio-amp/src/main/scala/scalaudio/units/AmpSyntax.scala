package scalaudio.units

import scalaudio.core.engine.AudioFunctionGraph
import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.core.{AudioContext, CoreSyntax}
import scalaudio.units.io.Playback

/**
  * Created by johnmcgill on 12/6/16.
  */
trait AmpSyntax extends CoreSyntax {

  // TODO: Create "map" on SignalProcessingGraph to chain new functions?
  def playback(frameFunc: Unit => Frame, duration: AudioDuration)(implicit audioContext: AudioContext): Unit = {
    val playbackFunc = Playback().asFunction.withModifier(Array(), (state: Frame, input: Frame) => input)

    AudioFunctionGraph(
      frameFunc.andThen(playbackFunc)
    ).play(duration)
  }
}
