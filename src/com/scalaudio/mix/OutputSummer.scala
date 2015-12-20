package com.scalaudio.mix

import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.unitgen.MonoSignalChain

/**
  * Created by johnmcgill on 12/19/15.
  */
case class OutputSummer(val sigChains : List[MonoSignalChain]) extends ScalaudioConfig {
  import OutputSummer._

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
    val bufferList : List[Array[Double]] = sigChains.map(x => x.outputBuffer)
    bufferList.tail.foldLeft(bufferList.head)((r,c) => sumBuffers(r,c))
  }
}

object OutputSummer {
  def sumBuffers(buffer1 : Array[Double], buffer2 : Array[Double]) : Array[Double] =
    (buffer1 zip buffer2).map{case (s1 : Double, s2 : Double) => s1 + s2}
}