package com.scalaudio.engine

import com.scalaudio.{Config, AudioContext}
import com.scalaudio.syntax.AudioDuration

/**
  * Created by johnmcgill on 1/22/16.
  */
trait MasterClockEngine {
  def outputBuffers : List[Array[Double]]

  def play(duration : AudioDuration)(implicit outputEngines : List[OutputEngine]) = {
    if (Config.AutoStartStop) start

    1 to duration.toBuffers.toInt foreach {_ =>
      AudioContext.advanceFrame

      if (Config.DebugEnabled && Config.ReportClipping && containsClipping(outputBuffers))
        println("CLIP!")

      outputEngines foreach (_.handleBuffer(outputBuffers))
    }

    if (Config.AutoStartStop) stop
  }

  def start = AudioContext.start
  def stop = AudioContext.stop

  def containsClipping(buffers :  List[Array[Double]]) : Boolean = {
    buffers foreach (b => if (!b.filter(x => Math.abs(x) > 1).isEmpty) return true)
    false
  }
}
