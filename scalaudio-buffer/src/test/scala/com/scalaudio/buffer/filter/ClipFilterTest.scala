package com.scalaudio.buffer.filter

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.mix.Splitter
import com.scalaudio.buffer.unitgen.{FuncGen, SineGen}
import com.scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import com.scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.duration._

/**
  * Created by johnmcgill on 12/26/15.
  */
class ClipFilterTest extends FlatSpec with Matchers with BufferSyntax {
  implicit val audioContext = AudioContext(ScalaudioConfig())

  "Clip filter" should "clip values to given min & max" in {
    val sineGen: SineGen = new SineGen()
    val clipper: ClipFilter = ClipFilter(-.2, .2)
    val frameFunc: () => List[Array[Double]] = () => sineGen.outputBuffers() chain clipper.processBuffers

    1 to 1000 foreach (_ => frameFunc() foreach (_ foreach (s => assert(s >= -.2 && s <= .2))))
  }

  "Clip filter" should "clip values to given min & max (play sample)" in {
    val sineGen: SineGen = new SineGen()
    val clipper: ClipFilter = ClipFilter(-.1, .8)
    val splitter: Splitter = Splitter(2)
    val testFrameFunc: () => List[Array[Double]] = () => sineGen.outputBuffers() chain clipper.processBuffers chain splitter.processBuffers

    BufferOutputTerminal(testFrameFunc).play(5 seconds)
  }
}
