package com.scalaudio.unitgen

/**
  * Created by johnmcgill on 12/21/15.
  */


import com.scalaudio.{ScalaudioConfig, AudioContext}
import com.scalaudio.engine.AudioTimeline
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.duration._

class GeneratorsTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "Noise Generator" should "create buffer of random noise on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val noiseGen = new NoiseGen with AudioTimeline
    noiseGen.play(5 seconds)
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sineGen = new SineGen(440 Hz) with AudioTimeline
    sineGen.play(1000 buffers)
  }

  "Square Generator" should "create buffer of squarewave on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val squareGen = new SquareGen(300 Hz) with AudioTimeline
    squareGen.play(1000 buffers)
  }

  "Sawtooth Generator" should "create buffer of sawtooth on every call" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val sawtoothGen = new SawtoothGen(880 * 2 Hz) with AudioTimeline
    sawtoothGen.play(1000 buffers)
  }

  "Signal chain" should "play noise" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(NOutChannels = 2))

    val chain = new SignalChain(new NoiseGen, List(Splitter(2))) with AudioTimeline
    chain.play(1000 buffers)
  }
}