package com.scalaudio.engine

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{UnitParams, AudioDuration}

/**
  * Created by johnmcgill on 1/22/16.
  */
trait AudioTimepiece {
  def outputBuffers(params : Option[UnitParams] = None)(implicit audioContext: AudioContext) : List[Array[Double]]

  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = {
    if (audioContext.config.AutoStartStop) start
    println("Started audio")

    1 to duration.toBuffers.toInt foreach {_ =>
      audioContext.advanceFrame

      if (audioContext.config.DebugEnabled && audioContext.config.ReportClipping && containsClipping(outputBuffers()))
        println("CLIP!")

      outputEngines foreach (_.handleBuffer(outputBuffers()))
    }

    if (audioContext.config.AutoStartStop) stop
    println("Stopped audio")
  }

  def start(implicit audioContext: AudioContext) = audioContext.start
  def stop(implicit audioContext: AudioContext) = audioContext.stop

  def containsClipping(buffers :  List[Array[Double]]) : Boolean = {
    buffers foreach (b => if (b.exists(x => Math.abs(x) > 1)) return true)
    false
  }
}
