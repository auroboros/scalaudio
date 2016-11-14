package scalaudio.core.math

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.{AudioContext, ScalaudioConfig}

/**
  * Created by johnmcgill on 1/10/16.
  */
class FftMathTest extends FlatSpec with Matchers {
  import scalaudio.core.math.window._

  "Windowing function" should "reproduce window given an input buffer of 1's" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val hanningWindow = new HannWindow(audioContext.config.fftSize).window

    val windowed = applyWindow(Array.fill(audioContext.config.fftSize)(1), hanningWindow)
    assert(windowed.deep == hanningWindow.deep) // or .sameElements?
  }
}