package com.scalaudio.engine

import com.scalaudio.{ScalaudioConfig, AudioContext}

/**
  * Playback trait -- mix this in to a UnitGen to make it playable
  *
  *  Created by johnmcgill on 12/20/15.
  */
trait Playback extends ScalaudioConfig {
  def outputBuffers : List[Array[Double]]

  def play(nFrames : Int) = {
    1 to nFrames foreach { _ =>
      val obs = outputBuffers
      if (obs.length != Channels){
        throw new Exception("Playback -- this device outputs incompatible number of channels. This playback system requires " + Channels)
      }
      AudioContext.audioOutput.write(obs(0))
    }
  }

  def stop = AudioContext.audioOutput.stop
}
