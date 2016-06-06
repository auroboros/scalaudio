package com.scalaudio.buffer.filter.util

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.unitgen.SineGen
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/24/15.
  */
class RangeScalerTest extends FlatSpec with Matchers with BufferSyntax {

  "Range Scaler" should "correctly scale range from -1, 1 to 7, 8" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val sineGen : SineGen = SineGen()
    val rangeScaler : RangeScaler = RangeScaler(7,8)

    val frameFunc: () => List[Array[Double]] = () => sineGen.outputBuffers() chain rangeScaler.processBuffers

    1 to 1000 foreach (_ => frameFunc() foreach (_ foreach (s => assert(s >= 7 && s <= 8))))
  }

}
