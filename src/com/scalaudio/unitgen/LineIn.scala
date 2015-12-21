package com.scalaudio.unitgen

import com.scalaudio.{Config, AudioContext}

/**
  * Created by johnmcgill on 12/20/15.
  */
class LineIn extends UnitGen {
  import LineIn._

  override def outputBuffers: List[Array[Double]] = {
    var buffer : Array[Double] = Array[Double](Config.FramesPerBuffer * Config.NChannels)
    AudioContext.audioInput.read(buffer)
    deinterleave(buffer)
  }
}

object LineIn {
  def deinterleave(buffer : Array[Double]) : List[Array[Double]] = {
    buffer.grouped(Config.NChannels).toArray.transpose.toList
  }
}