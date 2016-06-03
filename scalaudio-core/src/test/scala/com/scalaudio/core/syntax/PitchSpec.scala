package com.scalaudio.core.syntax

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 6/2/16.
  */
class PitchSpec extends FlatSpec with Matchers {
  "typical midi notes" should "be in audible range" in {
    println((50 to 127).map(n => Pitch.midiNote2Pitch(n).toHz))
  }
}
