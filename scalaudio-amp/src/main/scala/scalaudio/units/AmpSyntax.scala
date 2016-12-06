package scalaudio.units

import scalaudio.core.{AudioContext, CoreSyntax}
import scalaudio.core.engine.FunctionGraph
import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.units.io.Playback

/**
  * Created by johnmcgill on 12/6/16.
  */
trait AmpSyntax extends CoreSyntax {

  // TODO: Create "map" on SignalProcessingGraph to chain new functions?
  def playback(frameFunc: Unit => Frame, duration: AudioDuration)(implicit audioContext: AudioContext): Unit = {
    val playbackFunc = Playback().asFunction.withModifier(Array(), (state: Frame, input: Frame) => input)

    val combined = frameFunc.andThen(playbackFunc)

    FunctionGraph(
      combined
    ).play(duration)
  }
}
