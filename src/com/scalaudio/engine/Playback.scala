package com.scalaudio.engine


import com.scalaudio.syntax.AudioDuration
import com.scalaudio.{Config, AudioContext}

/**
  * Playback trait -- mix this in to a UnitGen to make it playable
  *
  *  Created by johnmcgill on 12/20/15.
  */
trait Playback {
  def outputBuffers : List[Array[Double]]

  def play(duration : AudioDuration) = {
    1 to duration.toBuffers.toInt foreach { _ =>
      AudioContext.advanceFrame

      val obs = outputBuffers // Fetch output buffers ONCE (multiple times will retrigger sig chain calcs)

      if (obs.length != Config.NOutChannels)
        throw new Exception("Playback -- this device outputs incompatible number of channels. This playback system requires " + Config.NOutChannels)

      if (Config.DebugEnabled && Config.ReportClipping && containsClipping(obs))
        println("CLIP!")

      AudioContext.audioOutput.write(Interleaver.interleave(obs))
    }
  }

  def start = AudioContext.start
  def stop = AudioContext.stop

  def containsClipping(buffers :  List[Array[Double]]) : Boolean = {
    buffers foreach (b => if (!b.filter(x => Math.abs(x) > 1).isEmpty) return true)
    false
  }
}
