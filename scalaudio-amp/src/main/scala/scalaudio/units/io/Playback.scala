package scalaudio.units.io

import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 12/5/16.
  */

case class Playback(implicit audioContext: AudioContext)
  extends SequentialState[Frame, AudioContext] {

  val c = audioContext.config
  val outBufferSize = c.framesPerBuffer * c.nOutChannels
  var bufferedOutput = Array.fill(outBufferSize)(0.0)
  var currentIndex = 0

  audioContext.startAudioIO()
  println("Started audio playback IO")

  // TODO: Add stop callback to cleanup object?

  override def nextState(frame: Frame)(implicit context: AudioContext): Frame = {
    frame.foreach { sample =>
      bufferedOutput(currentIndex) = sample
      currentIndex = (currentIndex + 1) % outBufferSize
    }

    if (currentIndex == 0) audioContext.audioOutput.write(bufferedOutput)

    frame
  }
}