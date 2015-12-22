package com.scalaudio.unitgen

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/19/15.
  */
case class SignalChain(val startGen : UnitGen, val filterChain : List[Filter]) extends UnitGen {
  val frameFunc = () => {
      if (filterChain.isEmpty) {
        startGen.outputBuffers
      } else {
        filterChain.foldLeft(startGen.outputBuffers)((r, c) => c.processBuffers(r))
      }
    }

  override def computeBuffer : List[Array[Double]] = frameFunc()
}