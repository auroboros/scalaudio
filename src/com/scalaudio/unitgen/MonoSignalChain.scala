package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/19/15.
  */
case class MonoSignalChain(val startGen : UnitGen, val filterChain : List[Filter]) extends UnitGen {
  val frameFunc = () => {
      if (filterChain.isEmpty) {
        startGen.outputBuffers
      } else {
        filterChain.foldLeft(startGen.outputBuffers)((r, c) => c.processBuffers(r))
      }
    }

  def outputBuffers : List[Array[Double]] = frameFunc()
}