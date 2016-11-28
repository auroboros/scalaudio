package scalaudio.buffer.io

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.buffer.BufferSyntax
import scalaudio.buffer.unitgen.LineIn
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, ScalaudioConfig}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with BufferSyntax {
  "LineIn" should "be able to passthrough audio" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nInChannels = 1, nOutChannels = 1))

    val passthrough = LineIn()

    StreamCollector(passthrough).play(100000 buffers)
  }
}
