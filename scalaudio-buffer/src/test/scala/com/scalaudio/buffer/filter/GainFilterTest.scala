package com.scalaudio.buffer.filter

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.util.RangeScaler
import com.scalaudio.buffer.unitgen.{FuncGen, SineGen, SquareGen, UnitGen}
import com.scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import com.scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/26/15.
  */
class GainFilterTest extends FlatSpec with Matchers with BufferSyntax {
  "Gain filter" should "multiply sine by slow sine" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen : SineGen = SineGen(440 Hz)
    val slowSineGen : SquareGen = new SquareGen(6 Hz) // SineGen(2) // new SquareGen(6)
    val gainFilter : GainFilter = GainFilter()
    val testFrameFunc: () => List[Array[Double]] = () => gainFilter.processBuffersWithSignal(sineGen.outputBuffers(), slowSineGen.outputBuffers())

    val output = BufferOutputTerminal(testFrameFunc, List(Playback()))
    Timeline.happen(10000 buffers, List(output))
  }

  "Gain filter" should "create tremolo when multiplying sine by scaled slow sine" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen : SineGen = SineGen(440 Hz)
    val slowSineGen : SineGen = SineGen(14 Hz) // new SquareGen(6)
    val gainFilter : GainFilter = GainFilter()
    val rangeScaler : RangeScaler = RangeScaler(0,1)
    val testFrameFunc: () => List[Array[Double]] = () => gainFilter.processBuffersWithSignal(sineGen.outputBuffers(), slowSineGen.outputBuffers() chain rangeScaler.processBuffers)

    val output = BufferOutputTerminal(testFrameFunc, List(Playback()))
    Timeline.happen(10000 buffers, List(output))
  }
}
