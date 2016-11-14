package scalaudio.buffer.unitgen

import java.io.File

import com.jsyn.data.FloatSample
import com.jsyn.util.SampleLoader._
import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.CoreSyntax
import scalaudio.core.util.{AdaptedJavaSoundSampleLoader, DoubleSample}

/**
  * Created by johnmcgill on 1/6/16.
  */
class SamplerTest extends FlatSpec with Matchers with CoreSyntax {

  "Sampler" should "come on come on get happy" in {
//    implicit val audioContext : AudioContext = AudioContext()
//
//    val wavFilename = "/Users/johnmcgill/nocode/samples/M1F1-int16-AFsp.wav"
//    val sampler = Sampler(List(wavFilename))
//
//    val dsOption : Option[DoubleSample] = sampler.soundSamples.get(wavFilename)
//    dsOption match {
//      case Some(ds) => println(ds.audioBuffers.head.length * audioContext.config.SamplingRate / ds.frameRate)
//      case None => throw new Exception("No sample...")
//    }
  }

  "Sampler" should "dump some buffers if frame rate is same?" in {
//    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(SamplingRate = 44100, NOutChannels = 2))
//    //SamplingRate = 44100 // 4000, 6000, 8000
//
//    val wavFilename = "/Users/johnmcgill/nocode/samples/Media-Convert_test5_PCM_Stereo_VBR_8SS_44100Hz.wav"
//    val sampler = new Sampler(List(wavFilename)) with AudioTimepiece
//
////    1 to 100 foreach (_ => println(sampler.outputBuffers))
//    sampler.play(3000 buffers)

  }

  "Math junk" should "work out" in {
    val res = (1 to 3).toList.map(_ => Array[Double](0,.1,.2))
    println(res)
  }

  "Scala DoubleSample" should "be same as JSyn FloatSample" in {
    val wavFilename = "/Users/johnmcgill/nocode/samples/M1F1-int16-AFsp.wav"
    val wavFile = new File(wavFilename)

    val doubleSample : DoubleSample = AdaptedJavaSoundSampleLoader.loadDoubleSample(wavFile)
    val floatSample : FloatSample = loadFloatSample(wavFile)

    val x = 1
  }
}