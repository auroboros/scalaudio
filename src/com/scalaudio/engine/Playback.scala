package com.scalaudio.engine

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 12/20/15.
  */
trait Playback {
  def outputBuffer : List[Array[Double]]

  def play(nFrames : Int) =
    1 to nFrames foreach {_ => AudioContext.audioOutput.write(outputBuffer(0))}

  def stop = AudioContext.audioOutput.stop
}
