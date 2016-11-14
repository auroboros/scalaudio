package scalaudio.buffer.types

import scala.concurrent.duration._
import scalaudio.buffer.BufferSyntax
import scalaudio.buffer.filter.mix.Splitter
import scalaudio.buffer.unitgen.{NoiseGen, SignalChain}
import scalaudio.core.engine.bufferwise.BufferOutputTerminal
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

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
