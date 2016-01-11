package com.scalaudio.math

import com.scalaudio.unitgen.SineGen
import org.apache.commons.math3.transform.{TransformType, DftNormalization, FastFourierTransformer}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/10/16.
  */
class ApacheFFTTest extends FlatSpec with Matchers {
  "Apache FFT" should "works its lil bunz off to get me a spectrum" in {
    val ffter = new FastFourierTransformer(DftNormalization.STANDARD)
    val sg = SineGen()

    val complexOut = ffter.transform(sg.computeBuffer(0), TransformType.FORWARD)
    println(complexOut)
  }
}
