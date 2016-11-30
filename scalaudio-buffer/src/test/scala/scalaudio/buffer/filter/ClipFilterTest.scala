package scalaudio.buffer.filter

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.buffer.BufferSyntax
import scalaudio.buffer.filter.mix.Splitter
import scalaudio.buffer.unitgen.SineGen
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, ScalaudioConfig}

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

    val stream = () => Stream.continually(testFrameFunc()).flatten

    StreamCollector(stream).play(5 seconds)
  }
}
