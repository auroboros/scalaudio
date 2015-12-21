package com.scalaudio.unitgen

/**
  * Created by johnmcgill on 12/21/15.
  */


import com.scalaudio.filter.mix.Splitter
import com.scalaudio.{Config, AudioContext}
import com.scalaudio.engine.Playback
import org.scalatest.{FlatSpec, Matchers}

class GeneratorsTest extends FlatSpec with Matchers {

  "Noise Generator" should "create buffer of random noise on every call" in {

    Config.NOutChannels = 1

    val noiseGen = new NoiseGen with Playback
    noiseGen.start
    noiseGen.play(1000)
    noiseGen.stop
  }

  "Sine Generator" should "create buffer of sine on every call" in {
    Config.NOutChannels = 1

    val sineGen = new SineGen(220) with Playback
    sineGen.start
    sineGen.play(1000)
    sineGen.stop
  }

  "Signal chain" should "play noise" in {
    Config.NOutChannels = 2

    val chain = new SignalChain(new NoiseGen, List(Splitter(2))) with Playback
    chain.start
    chain.play(1000)
    chain.stop
  }
}