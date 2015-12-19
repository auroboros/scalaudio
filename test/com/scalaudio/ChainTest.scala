package com.scalaudio

import com.scalaudio.filter.{Filter, GainFilter}
import com.scalaudio.unitgen.{SineGen, NoiseGen}
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/19/15.
  */
class ChainTest extends FlatSpec with Matchers with ScalaudioConfig {
  "List of modules" should "chain using outputBuffer" in {
    val filterChain : List[Filter] = List(new GainFilter(.5), new GainFilter(.75))
    val startUnit = new SineGen

    // Creates function variable to generate frames from this filter chain
    val frameFunc = () => filterChain.foldLeft(startUnit.outputBuffer)((r,c) => c.outputBuffer(r))

    1 to 1000 foreach {_ => AudioContext.audioOutput.write(frameFunc())}

    AudioContext.audioOutput.stop
  }
}