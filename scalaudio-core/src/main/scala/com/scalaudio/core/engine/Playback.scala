package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

/**
  * Playback trait -- mix this in to a UnitGen to make it playable
  *
  * Created by johnmcgill on 12/20/15.
  */
case class Playback()(implicit audioContext: AudioContext) extends OutputEngine {
  def start() = {
    audioContext.startAudioIO()
    println("Started audio playback IO")
  }

  def stop() = {
    audioContext.stopAudioIO()
    println("Stopped audio playback IO")
  }

  override def handleBuffers(buffers: Either[AudioSignal, MultichannelAudio]) = playAudio(buffers)

  def playAudio(buffers: Either[AudioSignal, MultichannelAudio]) = {
    buffers match {
      case Left(audioSignal) => audioContext.audioOutput.write(audioSignal)
      case Right(multichannelAudio) =>
        if (multichannelAudio.length != audioContext.config.nOutChannels)
          throw new Exception("Playback -- this device outputs incompatible number of channels. This playback system requires " + audioContext.config.nOutChannels)

        if (multichannelAudio.length > 1) // TODO: Can logical optimizations like this be macro-ized or does it have to be run-time?
          audioContext.audioOutput.write(Interleaver.interleave(multichannelAudio))
        else
          audioContext.audioOutput.write(multichannelAudio.head)
    }
  }
}
