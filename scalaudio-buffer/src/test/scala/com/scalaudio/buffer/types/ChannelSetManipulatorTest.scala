package com.scalaudio.buffer.types

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.buffer.filter.GainFilter
import com.scalaudio.buffer.filter.mix.StereoPanner
import com.scalaudio.buffer.unitgen.{FuncGen, NoiseGen, SineGen, UnitGen}
import com.scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import com.scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/22/15.
  */
class ChannelSetManipulatorTest extends FlatSpec with Matchers with BufferSyntax {

  "BufferFeeder rich type" should "kick in & feed dat buffer" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineGen : SineGen = SineGen(550 Hz)
    val panner : StereoPanner = StereoPanner()

//    bufferList2BufferFeeder(sineGen.outputBuffers) feed (panner.processBuffers(_))
    val outBuffers : List[Array[Double]] = sineGen.outputBuffers() chain panner.processBuffers
  }

  "Anon unit gen" should "be able to playback" in {
    implicit val audioContext = AudioContext(ScalaudioConfig())

    val noiseGen : NoiseGen = NoiseGen()
    val panner : StereoPanner = StereoPanner()

    //    bufferList2BufferFeeder(sineGen.outputBuffers) feed (panner.processBuffers(_))
    val testFrameFunc : () => List[Array[Double]] = () => {
      noiseGen.outputBuffers() chain panner.processBuffers
    }

    val output = BufferOutputTerminal(testFrameFunc, List(Playback()))
    Timeline.happen(1000 buffers, List(output))
  }

  "Several sine gens" should "be able to be mixed & played" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sg1 : SineGen = SineGen()
    val sg2 : SineGen = SineGen(873 Hz)
    val sg3 : SineGen = SineGen(521 Hz)
    val sg4 : SineGen = SineGen(921 Hz)
    val gain : GainFilter = GainFilter(.1)

    val playableUnitGen = new FuncGen({() => sg1.outputBuffers() mix sg2.outputBuffers() mix sg3.outputBuffers() mix sg4.outputBuffers() chain gain.processBuffers})

    val output = BufferOutputTerminal(playableUnitGen, List(Playback()))
    Timeline.happen(1000 buffers, List(output))
  }

  "Several stereo-panned sine gens" should "be able to be mixed & played" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val sg1 : SineGen = SineGen()
    val pan1 : StereoPanner = StereoPanner(.2)
    val sg2 : SineGen = SineGen(873 Hz)
    val pan2 : StereoPanner = StereoPanner(.7)
    val sg3 : SineGen = SineGen(521 Hz)
    val pan3 : StereoPanner = StereoPanner(0)
    val sg4 : SineGen = SineGen(921 Hz)
    val pan4 : StereoPanner = StereoPanner(1)
    val gain : GainFilter = GainFilter(.1)

    val playableUnitGen = new UnitGen {
      def computeBuffer(params : Option[UnitParams] = None) = (sg1.outputBuffers() chain pan1.processBuffers) mix
        (sg2.outputBuffers() chain pan2.processBuffers) mix
        (sg3.outputBuffers() chain pan3.processBuffers) mix
        (sg4.outputBuffers() chain pan4.processBuffers) chain gain.processBuffers
    }
    val output = BufferOutputTerminal(playableUnitGen, List(Playback()))
    Timeline.happen(1000 buffers, List(output))
  }

}