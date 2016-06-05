package com.scalaudio.core.math

import com.scalaudio.buffer.unitgen.SineGen
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 6/5/16.
  */
class FftTest extends FlatSpec with Matchers {
  "Apache FFT" should "works its lil bunz off to get me a spectrum" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val ffter = new FastFourierTransformer(DftNormalization.STANDARD)
    val sg = SineGen()

    val complexOut = ffter.transform(sg.outputBuffers().head, TransformType.FORWARD)
    println(complexOut)
  }
}
