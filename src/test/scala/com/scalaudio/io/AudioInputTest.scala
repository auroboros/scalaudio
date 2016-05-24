package com.scalaudio.io

import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.LineIn
import com.scalaudio.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "LineIn" should "be able to passthrough audio" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nInChannels = 1, nOutChannels = 1))

    val passthrough = LineIn()
    passthrough.play(100000 buffers)
  }
}
