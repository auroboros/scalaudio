package com.scalaudio.filter

import com.scalaudio.engine.AudioTimeline
import com.scalaudio.filter.util.RangeScaler
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen._
import com.scalaudio.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/26/15.
  */
class GainFilterTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Gain filter" should "multiply sine by slow sine" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sineGen : SineGen = SineGen(440 Hz)
    val slowSineGen : SquareGen = new SquareGen(6 Hz) // SineGen(2) // new SquareGen(6)
    val gainFilter : GainFilter = GainFilter()
    val testFrameFunc: () => List[Array[Double]] = () => gainFilter.processBuffersWithSignal(sineGen.outputBuffers(), slowSineGen.outputBuffers())

    val playableUnitGen = new UnitGen with AudioTimeline {def computeBuffer(params : Option[UnitParams] = None) = testFrameFunc()}
    playableUnitGen.play(10000 buffers)
  }

  "Gain filter" should "create tremolo when multiplying sine by scaled slow sine" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sineGen : SineGen = SineGen(440 Hz)
    val slowSineGen : SineGen = SineGen(14 Hz) // new SquareGen(6)
    val gainFilter : GainFilter = GainFilter()
    val rangeScaler : RangeScaler = RangeScaler(0,1)
    val testFrameFunc: () => List[Array[Double]] = () => gainFilter.processBuffersWithSignal(sineGen.outputBuffers(), slowSineGen.outputBuffers() feed rangeScaler.processBuffers)

    val playableUnitGen = new FuncGen(testFrameFunc) with AudioTimeline
    playableUnitGen.play(10000 buffers)
  }
}
