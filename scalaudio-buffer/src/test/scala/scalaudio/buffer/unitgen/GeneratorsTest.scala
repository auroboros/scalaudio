package scalaudio.buffer.unitgen

/**
  * Created by johnmcgill on 12/21/15.
  */

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.buffer.BufferSyntax
import scalaudio.buffer.filter.mix.Splitter
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, ScalaudioConfig}

class GeneratorsTest extends FlatSpec with Matchers with BufferSyntax {

  "Noise Generator" should "create buffer of random noise on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val noiseGen = new NoiseGen
    StreamCollector(noiseGen).play(5 seconds)
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen = new SineGen(440.Hz)
    StreamCollector(sineGen).play(1000.buffers)
  }

  "Square Generator" should "create buffer of squarewave on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val squareGen = new SquareGen(300.Hz)
    StreamCollector(squareGen).play(1000 buffers)
  }

  "Sawtooth Generator" should "create buffer of sawtooth on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sawtoothGen = new SawtoothGen((880 * 2).Hz)
    StreamCollector(sawtoothGen).play(1000 buffers)
  }

  "Signal chain" should "play noise" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val chain = new SignalChain(new NoiseGen, List(Splitter(2)))
    StreamCollector(chain).play(1000 buffers)
  }
}