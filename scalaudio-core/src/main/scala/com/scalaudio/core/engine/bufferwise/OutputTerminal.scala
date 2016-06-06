package com.scalaudio.core.engine.bufferwise

import com.scalaudio.core.AudioContext
import com.scalaudio.core.engine.OutputEngine
import com.scalaudio.core.types.{AudioDuration, MultichannelAudio}

/**
  * Created by johnmcgill on 5/22/16.
  */
trait OutputTerminal {
  def audioOut(implicit audioContext: AudioContext) : MultichannelAudio

  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = {
    if (audioContext.config.autoStartStop) audioContext.start()
    println("Started audio")

    1 to duration.toBuffers.toInt foreach {_ =>
      audioContext.advanceByBuffer()

      if (audioContext.config.debugEnabled && audioContext.config.reportClipping && containsClipping(audioOut))
        println("CLIP!")

      outputEngines foreach (_.handleBuffers(Right(audioOut)))
    }

    if (audioContext.config.autoStartStop) audioContext.stop()
    println("Stopped audio")
  }

  // TODO: Move to object somewhere?
  def containsClipping(buffers: MultichannelAudio) : Boolean = {
    buffers foreach (b => if (b.exists(x => Math.abs(x) > 1)) return true)
    false
  }
}