package com.scalaudio.engine

import com.scalaudio.AudioContext
import com.scalaudio.syntax.AudioDuration
import com.scalaudio.types.MultichannelAudio

/**
  * Created by johnmcgill on 5/22/16.
  */
trait OutputTerminal {
  def audioOut(implicit audioContext: AudioContext) : MultichannelAudio

  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = {
    if (audioContext.config.autoStartStop) audioContext.start()
    println("Started audio")

    1 to duration.toBuffers.toInt foreach {_ =>
      audioContext.advanceFrame()

      if (audioContext.config.debugEnabled && audioContext.config.reportClipping && containsClipping(audioOut))
        println("CLIP!")

      outputEngines foreach (_.handleBuffer(audioOut))
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