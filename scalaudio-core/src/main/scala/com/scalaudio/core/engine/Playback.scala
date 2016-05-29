package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

/**
  * Playback trait -- mix this in to a UnitGen to make it playable
  *
  * Created by johnmcgill on 12/20/15.
  */
case class Playback()(implicit audioContext: AudioContext) extends OutputEngine {
  override def handleAudio(buffers: MultichannelAudio) = playAudio(buffers)

  def playAudio(buffers: MultichannelAudio) = {
    if (buffers.length != audioContext.config.nOutChannels)
      throw new Exception("Playback -- this device outputs incompatible number of channels. This playback system requires " + audioContext.config.nOutChannels)

    if (buffers.length > 1) // TODO: Can logical optimizations like this be macro-ized or does it have to be run-time?
      audioContext.audioOutput.write(Interleaver.interleave(buffers))
    else
      audioContext.audioOutput.write(buffers.head)
  }

  override def handlePreInterleavedBuffer(buffer: AudioSignal) : Unit = audioContext.audioOutput.write(buffer)
}
