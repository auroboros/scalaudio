package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.filter.Filter
import com.scalaudio.syntax.UnitParams

/**
  * Created by johnmcgill on 12/19/15.
  */
case class SignalChain(val startGen : UnitGen, val filterChain : List[Filter])(implicit audioContext: AudioContext) extends UnitGen {
  val frameFunc = () => {
      if (filterChain.isEmpty) {
        startGen.outputBuffers()
      } else {
        filterChain.foldLeft(startGen.outputBuffers())((r, c) => c.processBuffers(r))
      }
    }

  // TODO: Maybe gens that use frameFuncs should have a mapping from that to internal, tested for efficiency?
  override def computeBuffer(params : Option[UnitParams] = None) = (internalBuffers = frameFunc())
}