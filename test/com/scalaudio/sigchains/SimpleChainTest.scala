package com.scalaudio.sigchains

import com.scalaudio.engine.Playback
import com.scalaudio.filter.mix.{StereoPanner, Splitter}
import com.scalaudio.filter.{Filter, GainFilter}
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.{NoiseGen, SignalChain, SineGen}
import com.scalaudio.{AudioContext, Config}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/19/15.
  */
class SimpleChainTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "SignalChain abstraction" should "playback mono SignalChain with nil filter list" in {
    Config.NOutChannels = 1

    val sigChain = new SignalChain(new NoiseGen, Nil) with Playback

    sigChain.play(1000 buffers)

    AudioContext.audioOutput.stop
  }

  "SignalChain abstraction" should "playback stereo SignalChain only if signal is split" in {
    Config.NOutChannels = 2

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2))) with Playback

    sigChain.play(1000 buffers)

    AudioContext.audioOutput.stop
  }

  "SignalChain abstraction" should "report clip for MonoSignalChain that clips" in {

    val sigChainList = List(SignalChain(new SineGen, Nil), SignalChain(new SineGen, Nil))
//    val summer = new Summer(sigChainList) with Playback

//    summer.play(1000)

    AudioContext.audioOutput.stop
  }

  "MonoSignalChain" should "be able to playback if given Playback trait" in {
    val filterChain : List[Filter] = List(new GainFilter(.5), new GainFilter(.75))
    val sigChainList = new SignalChain(new NoiseGen, filterChain) with Playback

    sigChainList.play(1000 buffers)
    sigChainList.stop
  }

  "Panner" should "pan some noise" in {
    val filterChain : List[Filter] = List(new StereoPanner(.5))
    val sigChainList = new SignalChain(new NoiseGen, filterChain) with Playback

    sigChainList.play(1000 buffers)
    sigChainList.stop
  }
}