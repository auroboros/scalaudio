package com.scalaudio.unitgen

import com.scalaudio.{Config, AudioContext}

/**
  * Created by johnmcgill on 12/20/15.
  */
class LineIn extends UnitGen {
  import LineIn._

  override def outputBuffers: List[Array[Double]] = {
    var buffer : Array[Double] = Array[Double](Config.FramesPerBuffer * Config.NInChannels)
    AudioContext.audioInput.read(buffer)
    if (Config.NInChannels > 1) deinterleave(buffer) else List(buffer)
  }
}

object LineIn {
  def deinterleave(buffer : Array[Double]) : List[Array[Double]] = {
    buffer.grouped(Config.NInChannels).toArray.transpose.toList
  }
}