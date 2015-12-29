package com.scalaudio.syntax

import com.scalaudio.Config
import com.scalaudio.engine.Playback
import com.scalaudio.filter.GainFilter
import com.scalaudio.filter.mix.StereoPanner
import com.scalaudio.unitgen.{NoiseGen, SineGen, UnitGen}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/22/15.
  */
class ChannelSetManipulatorTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

  "BufferFeeder rich type" should "kick in & feed dat buffer" in {
    val sineGen : SineGen = SineGen(550)
    val panner : StereoPanner = StereoPanner()

//    bufferList2BufferFeeder(sineGen.outputBuffers) feed (panner.processBuffers(_))
    val outBuffers : List[Array[Double]] = sineGen.outputBuffers feed panner.processBuffers
  }

  "Anon unit gen" should "be able to playback" in {
    val noiseGen : NoiseGen = NoiseGen()
    val panner : StereoPanner = StereoPanner()

    //    bufferList2BufferFeeder(sineGen.outputBuffers) feed (panner.processBuffers(_))
    val testFrameFunc : () => List[Array[Double]] = () => {
      noiseGen.outputBuffers feed panner.processBuffers
    }

    val playableUnitGen = new UnitGen with Playback {def computeBuffer = testFrameFunc()}
    playableUnitGen.play(1000)
  }

  "Several sine gens" should "be able to be mixed & played" in {
    Config.NOutChannels = 1

    val sg1 : SineGen = SineGen()
    val sg2 : SineGen = SineGen(873)
    val sg3 : SineGen = SineGen(521)
    val sg4 : SineGen = SineGen(921)
    val gain : GainFilter = GainFilter(.1)

    val playableUnitGen = new UnitGen with Playback {
      def computeBuffer = sg1.outputBuffers mix sg2.outputBuffers mix sg3.outputBuffers mix sg4.outputBuffers feed gain.processBuffers
    }
    playableUnitGen.play(1000)
  }

  "Several stereo-panned sine gens" should "be able to be mixed & played" in {
    Config.NOutChannels = 2

    val sg1 : SineGen = SineGen()
    val pan1 : StereoPanner = StereoPanner(.2)
    val sg2 : SineGen = SineGen(873)
    val pan2 : StereoPanner = StereoPanner(.7)
    val sg3 : SineGen = SineGen(521)
    val pan3 : StereoPanner = StereoPanner(0)
    val sg4 : SineGen = SineGen(921)
    val pan4 : StereoPanner = StereoPanner(1)
    val gain : GainFilter = GainFilter(.1)

    val playableUnitGen = new UnitGen with Playback {
      def computeBuffer = (sg1.outputBuffers feed pan1.processBuffers) mix
        (sg2.outputBuffers feed pan2.processBuffers) mix
        (sg3.outputBuffers feed pan3.processBuffers) mix
        (sg4.outputBuffers feed pan4.processBuffers) feed gain.processBuffers
    }
    playableUnitGen.play(1000)
  }

}