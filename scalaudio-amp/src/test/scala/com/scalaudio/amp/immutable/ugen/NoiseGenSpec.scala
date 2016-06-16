package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.ScalaudioCoreTestHarness

/**
  * Created by johnmcgill on 6/16/16.
  */
class NoiseGenSpec extends ScalaudioCoreTestHarness {
  "Generated sample" should "be between -1 and 1" in {
    1 to 1000 foreach { _ =>
      NoiseGen.generateSample should be <= 1.0
      NoiseGen.generateSample should be >= -1.0
    } // TODO: check distribution? Avg... something
  }

  "Generated frame" should "be different values" in {
    val frame = NoiseGen.generateFrame(10)

    frame.forall(_ == frame.head) shouldBe false // all elements shouldn't be equal
    frame.length shouldBe 10
  }
}
