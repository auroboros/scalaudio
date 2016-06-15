package com.scalaudio.core.math

import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/10/16.
  */
class FFTMathTest extends FlatSpec with Matchers {
  import window._

  "Windowing function" should "reproduce window given an input buffer of 1's" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val windowed = applyWindow(Array.fill(audioContext.config.fftBufferSize)(1), FFTMath.windowForConfig)
    assert(windowed.deep == FFTMath.windowForConfig.deep) // or .sameElements?
  }
}