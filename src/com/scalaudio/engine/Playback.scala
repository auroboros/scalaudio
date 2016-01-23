package com.scalaudio.engine


import com.scalaudio.syntax.AudioDuration
import com.scalaudio.{Config, AudioContext}

import scala.concurrent
import scala.concurrent.duration

/**
  * Playback trait -- mix this in to a UnitGen to make it playable
  *
  *  Created by johnmcgill on 12/20/15.
  */
case class Playback() extends OutputEngine {
  def handleBuffer(buffers : List[Array[Double]]) = play(buffers)

  def play(buffers : List[Array[Double]]) = {
      if (buffers.length != Config.NOutChannels)
        throw new Exception("Playback -- this device outputs incompatible number of channels. This playback system requires " + Config.NOutChannels)

      AudioContext.audioOutput.write(Interleaver.interleave(buffers))
  }
}
