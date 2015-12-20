package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/19/15.
  */
case class MonoSignalChain(val startGen : UnitGen, val filterChain : List[Filter]) extends UnitGen {
  val frameFunc = () => {
      if (filterChain.isEmpty) {
        startGen.outputBuffer
      } else {
        filterChain.foldLeft(startGen.outputBuffer)((r, c) => c.processBuffer(r))
      }
    }

  def outputBuffer : List[Array[Double]] = List(frameFunc())
}