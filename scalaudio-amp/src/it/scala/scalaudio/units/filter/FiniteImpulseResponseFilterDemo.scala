package scalaudio.units.filter

import scalaudio.core.AudioContext
import scalaudio.units.AmpTestHarness
import scalaudio.units.sampler.{FileSample, TriggerSampler, TriggerSamplerState}
import scalaz.Scalaz._
import scalaz._
import scala.concurrent.duration._
/**
  * Created by johnmcgill on 1/1/17.
  */
class FiniteImpulseResponseFilterDemo extends AmpTestHarness {
  "Fir" should "delay when given simple delay characteristic" in {

//    val ff = Sine(440.Hz).asReflexiveFunction()
//      .map(FiniteImpulseResponseFilter().asReflexiveFunction())

    implicit val audioContext = AudioContext()

//    val coeffs: List[Double] = (1.0 :: List.fill(44100)(0.0)) :+ 0.7

    val coeffs: List[Double] = 1.0 :: List.fill(4000)(0.0) ::: List(1.0)

    val ff = TriggerSampler.immutable(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))
      .asFunction(TriggerSamplerState(playbackPositions = List(0)))
      .map(_.frame.head)
      .map(FiniteImpulseResponseFilter(coeffs.toArray).asReflexiveFunction())
      .map(_._1)
      .map(Array.fill(2)(_))

    playback(ff, 6.seconds)
  }
}