package com.scalaudio.buffer.sigchains

import com.scalaudio.buffer.filter.{Filter, GainFilter}
import com.scalaudio.buffer.filter.mix.{Splitter, StereoPanner}
import com.scalaudio.buffer.unitgen.{NoiseGen, SignalChain, SineGen}
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/19/15.
  */
class SimpleChainTest extends FlatSpec with Matchers with CoreSyntax {

  "SignalChain abstraction" should "playback mono SignalChain with nil filter list" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sigChain = new SignalChain(new NoiseGen, Nil)

    sigChain.play(1000 buffers)
  }

  "SignalChain abstraction" should "playback stereo SignalChain only if signal is split" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2)))

    sigChain.play(1000 buffers)
  }

  "SignalChain abstraction" should "report clip for MonoSignalChain that clips" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val sigChainList = List(SignalChain(new SineGen, Nil), SignalChain(new SineGen, Nil))
//    val summer = new Summer(sigChainList) with Playback

//    summer.play(1000)
  }

  "MonoSignalChain" should "be able to playback if given Playback trait" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val filterChain : List[Filter] = List(new GainFilter(.5), new GainFilter(.75))
    val sigChainList = new SignalChain(new NoiseGen, filterChain)

    sigChainList.play(1000 buffers)
  }

  "Panner" should "pan some noise" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val filterChain : List[Filter] = List(new StereoPanner(.5))
    val sigChainList = new SignalChain(new NoiseGen, filterChain)

    sigChainList.play(1000 buffers)
  }
}