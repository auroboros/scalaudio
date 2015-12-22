package com.scalaudio.sigchains

import com.scalaudio.engine.Playback
import com.scalaudio.filter.mix.StereoPanner
import com.scalaudio.implicits.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.{NoiseGen, SineGen, UnitGen}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/22/15.
  */
class BufferFeederTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {

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
    val testFrameFunc : () => List[Array[Double]] = () => { noiseGen.outputBuffers feed panner.processBuffers}

    val playableUnitGen = new UnitGen with Playback {def computeBuffer = testFrameFunc()}
    playableUnitGen.play(1000)
  }

}