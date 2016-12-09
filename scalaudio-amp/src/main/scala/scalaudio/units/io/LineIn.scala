package scalaudio.units.io

import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.types.{Frame, Sample}

/**
  * Created by johnmcgill on 12/5/16.
  */
case class LineInState(
                        frame: Frame,
                        buffer: Array[Sample],
                        frameIndex: Int
                      )

object LineInState {
  def initialState(implicit audioContext: AudioContext): LineInState = {
    var newBuffer: Array[Double] = Array[Double](audioContext.config.framesPerBuffer * audioContext.config.nInChannels)
    audioContext.audioIO.read(newBuffer)

    new LineInState(
      Array(),
      newBuffer,
      0
    )
  }
}

object LineIn extends SequentialState[LineInState, AudioContext] {

  def nextState(state: LineInState)(implicit audioContext: AudioContext): LineInState =
    if (state.frameIndex == audioContext.config.framesPerBuffer - 1) {
      val newBuffer: Array[Double] = Array[Double](audioContext.config.framesPerBuffer * audioContext.config.nInChannels)
      audioContext.audioIO.read(newBuffer)
      state.copy(
        frame = Array(0), // TODO: Get actual output frame
        buffer = newBuffer,
        frameIndex = 0
      )
    } else {
      state.copy(
        frame = Array(0), // TODO: Get actual output frame
        frameIndex = state.frameIndex + 1
      )
    }

}
