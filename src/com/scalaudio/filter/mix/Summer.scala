package com.scalaudio.filter.mix

import com.scalaudio.filter.Filter
import com.scalaudio.{Config, AudioContext}
import com.scalaudio.unitgen.SignalChain

/**
  * Created by johnmcgill on 12/19/15.
  */
case class Summer() extends Filter {
  import Summer._

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] =
    List(inBuffers.tail.foldLeft(inBuffers.head)((r,c) => sumBuffers(r,c)))
}

object Summer {
  def sumBuffers(buffer1 : Array[Double], buffer2 : Array[Double]) : Array[Double] =
    (buffer1 zip buffer2).map{case (s1 : Double, s2 : Double) => s1 + s2}
}