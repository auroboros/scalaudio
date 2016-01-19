package com.scalaudio.filter

import com.scalaudio.engine.Playback
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.{SineGen, UnitGen}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/26/15.
  */
class ClipFilterTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Clip filter" should "clip values to given min & max" in {
    val sineGen : SineGen = new SineGen()
    val clipper : ClipFilter = ClipFilter(-.2, .2)
    val frameFunc: () => List[Array[Double]] = () => sineGen.outputBuffers feed clipper.processBuffers

    1 to 1000 foreach (_ => frameFunc() foreach (_ foreach (s => assert(s >= -.2 && s <= .2))))
  }

  "Clip filter" should "clip values to given min & max (play sample)" in {
    val sineGen : SineGen = new SineGen()
    val clipper : ClipFilter = ClipFilter(-.1, .8)
    val splitter : Splitter = Splitter(2)
    val testFrameFunc: () => List[Array[Double]] = () => sineGen.outputBuffers feed clipper.processBuffers feed splitter.processBuffers

    val playableUnitGen = new UnitGen with Playback {def computeBuffer = testFrameFunc()}
    playableUnitGen.play(1000 buffers)
  }
}
