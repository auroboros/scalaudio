package com.scalaudio.core.unitgen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.filter.Filter

/**
  * Created by johnmcgill on 12/19/15.
  */
// TODO: This class may be obsolete now that FuncGen exists
case class SignalChain(startGen : UnitGen, filterChain : List[Filter])(implicit audioContext: AudioContext) extends UnitGen {
  val frameFunc = () => {
      if (filterChain.isEmpty) {
        startGen.outputBuffers()
      } else {
        filterChain.foldLeft(startGen.outputBuffers())((r, c) => c.processBuffers(r))
      }
    }

  // TODO: Maybe gens that use frameFuncs should have a mapping from that to internal, tested for efficiency?
  override def computeBuffer(params : Option[UnitParams] = None) = internalBuffers = frameFunc()
}