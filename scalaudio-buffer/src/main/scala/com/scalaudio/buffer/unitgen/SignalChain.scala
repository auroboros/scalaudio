package com.scalaudio.buffer.unitgen

import com.scalaudio.buffer.filter.Filter
import com.scalaudio.core.AudioContext

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