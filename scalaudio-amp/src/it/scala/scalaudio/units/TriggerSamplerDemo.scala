package scalaudio.units

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.AudioContext
import scalaudio.units.sampler.{FileSample, TriggerSampler, TriggerSamplerState}
import scalaz._
import Scalaz._
import scala.concurrent.duration._
/**
  * Created by johnmcgill on 12/23/16.
  */
class TriggerSamplerDemo extends FlatSpec with Matchers with AmpSyntax {
  "TriggerSampler" should "play a single sample if initialized with a single 0 position list entry" in {
    implicit val audioContext = AudioContext()

    val ff = TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))
      .asFunction(TriggerSamplerState(playbackPositions = List(0)))
      .map(_.frame)

    playback(ff, 5.seconds)
  }
}
