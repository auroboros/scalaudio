package com.scalaudio.unitgen

/**
  * Created by johnmcgill on 12/21/15.
  */


import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

class GeneratorsTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "Noise Generator" should "create buffer of random noise on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val noiseGen = new NoiseGen
    noiseGen.play(5.seconds)
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen = new SineGen(440.Hz)
    sineGen.play(1000.buffers)
  }

  "Square Generator" should "create buffer of squarewave on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val squareGen = new SquareGen(300.Hz)
    squareGen.play(1000.buffers)
  }

  "Sawtooth Generator" should "create buffer of sawtooth on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sawtoothGen = new SawtoothGen((880 * 2).Hz)
    sawtoothGen.play(1000.buffers)
  }

  "Signal chain" should "play noise" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val chain = new SignalChain(new NoiseGen, List(Splitter(2)))
    chain.play(1000.buffers)
  }
}