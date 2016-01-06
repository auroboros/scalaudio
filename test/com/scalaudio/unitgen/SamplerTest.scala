package com.scalaudio.unitgen

import com.scalaudio.Config
import com.scalaudio.engine.Playback
import com.scalaudio.jsyn.util.DoubleSample
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/6/16.
  */
class SamplerTest extends FlatSpec with Matchers {
  "Sampler" should "come on come on get happy" in {
    val wavFilename = "/Users/johnmcgill/nocode/samples/M1F1-int16-AFsp.wav"
    val sampler = Sampler(List(wavFilename))

    val dsOption : Option[DoubleSample] = sampler.soundSamples.get(wavFilename)
    dsOption match {
      case Some(ds) => println(ds.audioBuffers(0).size * Config.SamplingRate / ds.frameRate)
    }
  }

  "Sampler" should "dump some buffers if frame rate is same?" in {
    Config.SamplingRate = 8000 // 4000, 6000, 8000
    Config.NOutChannels = 2

    val wavFilename = "/Users/johnmcgill/nocode/samples/M1F1-int16-AFsp.wav"
    val sampler = new Sampler(List(wavFilename)) with Playback

//    1 to 100 foreach (_ => println(sampler.outputBuffers))
    sampler.play(3000)

  }

  "Math junk" should "work out" in {
    val res = (1 to 3).toList.map(_ => Array[Double](0,.1,.2))
    println(res)
  }
}
