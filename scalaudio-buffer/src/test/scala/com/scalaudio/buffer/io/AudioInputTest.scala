package com.scalaudio.buffer.io

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.unitgen.LineIn
import scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with BufferSyntax {
  "LineIn" should "be able to passthrough audio" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nInChannels = 1, nOutChannels = 1))

    val passthrough = LineIn()

    BufferOutputTerminal(passthrough).play(100000 buffers)
  }
}
