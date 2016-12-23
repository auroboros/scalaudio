package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaz._
import Scalaz._
import scalaudio.units.AmpSyntax

/**
  * Created by johnmcgill on 5/29/16.
  */
class SawtoothStateGenDemo extends FlatSpec with Matchers with AmpSyntax {
  "Sawtooth state gen" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val frameFunc = Sawtooth.immutable.asFunction(OscState(0, 440.Hz, 0))
      .map { s =>
        println(s.sample)
        s.sample
      }.map(Array(_))

    playback(frameFunc, 5 seconds)
  }
}
