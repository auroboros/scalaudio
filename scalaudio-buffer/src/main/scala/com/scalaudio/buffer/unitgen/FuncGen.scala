package com.scalaudio.buffer.unitgen

import com.scalaudio.core.types.MultichannelAudio

/**
  * FuncGen - a utility unit generator that generates buffers by executing the given "bufferFunc"
  *
  * Created by johnmcgill on 1/18/16.
  */
case class FuncGen(bufferFunc : () => MultichannelAudio) extends UnitGen {
  override def computeBuffer(params : Option[UnitParams] = None) = internalBuffers = bufferFunc()
}
