package com.scalaudio.util

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/21/15.
  */
class PhaseMathTest extends FlatSpec with Matchers {
  "Phi" should "increment in radians" in {
    val phi : Double = (2.0 * Math.PI) / 44100.0 / 440.0 / 32.0 //(2 * Math.PI)
    println(phi)
  }
}