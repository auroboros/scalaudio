package com.scalaudio

import com.scalaudio.filter.{Filter, GainFilter}
import com.scalaudio.filter.mix.{Splitter, Summer}
import com.scalaudio.engine.Playback
import com.scalaudio.unitgen.{SignalChain, SineGen, NoiseGen}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/19/15.
  */
class ChainTest extends FlatSpec with Matchers {
  "List of modules" should "chain using outputBuffer" in {
//    val filterChain : List[Filter] = List(new GainFilter(.5), new GainFilter(.75))
//    val startUnit = new SineGen
//
//    // Creates function variable to generate frames from this filter chain
//    val frameFunc = () => filterChain.foldLeft(startUnit.outputBuffers)((r, c) => c.processBuffers(r))
//
//    1 to 1000 foreach {_ => AudioContext.audioOutput.write(frameFunc())}
//
//    AudioContext.audioOutput.stop
  }

  "SignalChain abstraction" should "playback in same fashion" in {
    val filterChain : List[Filter] = List(new GainFilter(.5), new GainFilter(.75))

    val sigChainList = List(SignalChain(new NoiseGen, filterChain), SignalChain(new SineGen, filterChain))
//    val summer = Summer(sigChainList)

//    summer.play(1000)

    AudioContext.audioOutput.stop
  }

  "SignalChain abstraction" should "playback mono SignalChain with nil filter list" in {
    Config.NOutChannels = 1

    val sigChain = new SignalChain(new NoiseGen, Nil) with Playback

    sigChain.play(1000)

    AudioContext.audioOutput.stop
  }

  "SignalChain abstraction" should "playback stereo SignalChain only if signal is split" in {
    Config.NOutChannels = 2

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2))) with Playback

    sigChain.play(1000)

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

    sigChainList.play(1000)
    sigChainList.stop
  }
}