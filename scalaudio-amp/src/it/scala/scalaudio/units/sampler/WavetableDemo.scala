package scalaudio.units.sampler

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, ScalaudioConfig}
import scalaudio.units.AmpSyntax
import scalaz.Scalaz._
import scalaz._

/**
  * Created by johnmcgill on 12/23/16.
  */
class WavetableDemo extends FlatSpec with Matchers with AmpSyntax {
  "Wavetable" should "loop a sample by default" in {
    implicit val audioContext = AudioContext()

    val ff = Wavetable(
      FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"),
      10
    ).asFunction(WavetableState(Array.empty, 0)).map(_.frame)

    playback(ff, 10.seconds)
  }

  "Wavetable" should "loop a sine table" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val ff = Wavetable(SineTable, 440) // TODO: If sampled period is 10 seconds, shouldnt this sound lower?
      .asFunction(WavetableState(Array.empty, 0))
      .map(_.frame)

    playback(ff, 10.seconds)
  }
}
