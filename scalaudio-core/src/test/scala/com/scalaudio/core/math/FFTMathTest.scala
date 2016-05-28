package com.scalaudio.core.math

import com.scalaudio.core.unitgen.SineGen
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/10/16.
  */
class FFTMathTest extends FlatSpec with Matchers {
  "Apache FFT" should "works its lil bunz off to get me a spectrum" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val ffter = new FastFourierTransformer(DftNormalization.STANDARD)
    val sg = SineGen()

    val complexOut = ffter.transform(sg.outputBuffers().head, TransformType.FORWARD)
    println(complexOut)
  }

  "Windowing function" should "reproduce window given an input buffer of 1's" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val windowed = FFTMath.applyWindow(Array.fill(audioContext.config.fftBufferSize)(1))
    assert(windowed.deep == FFTMath.window.deep) // or .sameElements?
  }
}