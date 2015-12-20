package com.scalaudio.mix

import com.scalaudio.AudioContext
import com.scalaudio.unitgen.MonoSignalChain

/**
  * Created by johnmcgill on 12/19/15.
  */
case class OutputSummer(val sigChains : List[MonoSignalChain]) {
  import OutputSummer._

  def play(numFrames : Int) = {
    1 to numFrames foreach {_ => AudioContext.audioOutput.write(summedOutputBuffer)}
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