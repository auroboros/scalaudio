package com.scalaudio.unitgen

/**
  * Created by johnmcgill on 12/21/15.
  */


import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.engine.AudioTimepiece
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

class GeneratorsTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "Noise Generator" should "create buffer of random noise on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val noiseGen = new NoiseGen with AudioTimepiece
    noiseGen.play(1000 buffers)
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sineGen = new SineGen(440 Hz) with AudioTimepiece
    sineGen.play(1000 buffers)
  }

  "Square Generator" should "create buffer of squarewave on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val squareGen = new SquareGen(300 Hz) with AudioTimepiece
    squareGen.play(1000 buffers)
  }

  "Sawtooth Generator" should "create buffer of sawtooth on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sawtoothGen = new SawtoothGen(880 * 2 Hz) with AudioTimepiece
    sawtoothGen.play(1000 buffers)
  }

  "Signal chain" should "play noise" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 2))

    val chain = new SignalChain(new NoiseGen, List(Splitter(2))) with AudioTimepiece
    chain.play(1000 buffers)
  }
}