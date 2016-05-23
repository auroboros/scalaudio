package com.scalaudio.engine

import com.scalaudio.AudioContext
import com.scalaudio.syntax.AudioDuration
import com.scalaudio.types.MultichannelAudio
import com.scalaudio.unitgen.OutputTerminal

/**
  * Created by johnmcgill on 1/22/16.
  */
trait AudioTimeline {
  self : OutputTerminal =>

  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = {
    if (audioContext.config.AutoStartStop) start
    println("Started audio")

    1 to duration.toBuffers.toInt foreach {_ =>
      audioContext.advanceFrame()

      if (audioContext.config.DebugEnabled && audioContext.config.ReportClipping && containsClipping(audioOut))
        println("CLIP!")

      outputEngines foreach (_.handleBuffer(audioOut))
    }

    if (audioContext.config.AutoStartStop) stop
    println("Stopped audio")
  }

  def start(implicit audioContext: AudioContext) = audioContext.start()
  def stop(implicit audioContext: AudioContext) = audioContext.stop()

  def containsClipping(buffers: MultichannelAudio) : Boolean = {
    buffers foreach (b => if (b.exists(x => Math.abs(x) > 1)) return true)
    false
  }
}
