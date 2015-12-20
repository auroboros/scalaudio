package com.scalaudio.filter.mix

import com.scalaudio.filter.Filter
import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.unitgen.MonoSignalChain

/**
  * Created by johnmcgill on 12/19/15.
  */
case class Summer(val sigChains : List[MonoSignalChain]) extends Filter with ScalaudioConfig {
  import Summer._

  def play(numFrames : Int) = {
    1 to numFrames foreach {_ =>
      val playbackBuffer = summedOutputBuffer
      if (ReportClipping) {
        if (!playbackBuffer.filter(x => Math.abs(x) >= 1).isEmpty) println("CLIP")
      }
      AudioContext.audioOutput.write(playbackBuffer)
    }
  }

  def summedOutputBuffer = {
    val bufferList : List[Array[Double]] = sigChains.map(x => x.outputBuffers(0))
    bufferList.tail.foldLeft(bufferList.head)((r,c) => sumBuffers(r,c))
  }

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = ???
}

object Summer {
  def sumBuffers(buffer1 : Array[Double], buffer2 : Array[Double]) : Array[Double] =
    (buffer1 zip buffer2).map{case (s1 : Double, s2 : Double) => s1 + s2}
}