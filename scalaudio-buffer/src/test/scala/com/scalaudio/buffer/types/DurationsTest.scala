package com.scalaudio.buffer.types

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.mix.Splitter
import com.scalaudio.buffer.unitgen.{NoiseGen, SignalChain}
import com.scalaudio.core.{AudioContext, IntegrationTestHarness, ScalaudioConfig}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 6/5/16.
  */
class DurationsTest extends IntegrationTestHarness with BufferSyntax {
  "Durations syntax sugar" should "convert a finite duration to samples (int or long)" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2)))

    sigChain.play(5 seconds) // THIS IS COMPUTING SAMPLES YET PLAY WANTS FRAMES...
  }
}
