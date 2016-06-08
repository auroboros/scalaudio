package com.scalaudio.buffer.io

import com.scalaudio.buffer.unitgen.LineIn
import com.scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import com.scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with CoreSyntax {
  "LineIn" should "be able to passthrough audio" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nInChannels = 1, nOutChannels = 1))

    val passthrough = LineIn()
    val output = BufferOutputTerminal(() => passthrough.outputBuffers(), List(Playback()))
    Timeline.happen(100000 buffers, List(output))
  }
}
