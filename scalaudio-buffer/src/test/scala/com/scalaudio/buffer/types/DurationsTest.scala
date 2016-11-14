package com.scalaudio.buffer.types

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.mix.Splitter
import com.scalaudio.buffer.unitgen.{NoiseGen, SignalChain}
import scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 6/5/16.
  */
class DurationsTest extends ScalaudioCoreTestHarness with BufferSyntax {
  "Durations syntax sugar" should "convert a finite duration to samples (int or long)" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2)))

    BufferOutputTerminal(sigChain).play(5 seconds)
  }
}
