package com.scalaudio.math

import com.scalaudio.Config
import com.scalaudio.unitgen.SineGen
import org.apache.commons.math3.transform.{TransformType, DftNormalization, FastFourierTransformer}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/10/16.
  */
class FFTMathTest extends FlatSpec with Matchers {
  "Apache FFT" should "works its lil bunz off to get me a spectrum" in {
    val ffter = new FastFourierTransformer(DftNormalization.STANDARD)
    val sg = SineGen()

    val complexOut = ffter.transform(sg.outputBuffers(0), TransformType.FORWARD)
    println(complexOut)
  }

  "Windowing function" should "reproduce window given an input buffer of 1's" in {
    val windowed = FFTMath.applyWindow(Array.fill(Config.FFTBufferSize)(1))
    assert(windowed.deep == FFTMath.window.deep) // or .sameElements?
  }
}