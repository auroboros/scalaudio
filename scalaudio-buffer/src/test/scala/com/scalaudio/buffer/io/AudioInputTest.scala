package com.scalaudio.buffer.io

import com.scalaudio.buffer.unitgen.LineIn
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with CoreSyntax {
  "LineIn" should "be able to passthrough audio" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nInChannels = 1, nOutChannels = 1))

    val passthrough = LineIn()
    passthrough.play(100000 buffers)
  }
}
