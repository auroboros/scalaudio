package com.scalaudio.filter.mix

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/19/15.
  */
case class Summer() extends Filter {
  import Summer._

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] =
    List(inBuffers.tail.foldLeft(inBuffers.head)((r,c) => sumBuffers(r,c)))

  // Multichannel sum (overlay stereo on stereo, for example)
  def processBuffersWithSignal(inBuffers: List[Array[Double]], sigCtrlBuffers : List[Array[Double]]): List[Array[Double]] =
    sumMultichannelBuffers(inBuffers, sigCtrlBuffers)
}

object Summer {
  def sumBuffers(buffer1 : Array[Double], buffer2 : Array[Double]) : Array[Double] =
    (buffer1 zip buffer2).map{case (s1 : Double, s2 : Double) => s1 + s2}

  def sumMultichannelBuffers(inBuffers: List[Array[Double]], sigCtrlBuffers : List[Array[Double]]): List[Array[Double]] = {
    if (inBuffers.size != sigCtrlBuffers.size) throw new Exception(s"Size mismatch (inBuffers : ${inBuffers.size}, ctrlBuffers : ${sigCtrlBuffers.size}")
    (inBuffers zip sigCtrlBuffers) map {case (sigBuff : Array[Double], ctrlBuff : Array[Double]) => (sigBuff zip ctrlBuff) map
      {case (sig : Double, ctrl : Double) => sig + ctrl}}
  }
}