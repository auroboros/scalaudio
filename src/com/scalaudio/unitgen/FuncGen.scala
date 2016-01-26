package com.scalaudio.unitgen

import com.scalaudio.syntax.UnitParams

/**
  * FuncGen - a utility unit generator that generates buffers by executing the given "bufferFunc"
  *
  * Created by johnmcgill on 1/18/16.
  */
case class FuncGen(val bufferFunc : () => List[Array[Double]]) extends UnitGen {
  override def computeBuffer(params : Option[UnitParams] = None) = (internalBuffers = bufferFunc())
}
