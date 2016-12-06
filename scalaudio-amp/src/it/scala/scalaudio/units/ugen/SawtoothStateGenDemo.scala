package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 5/29/16.
  */
class SawtoothStateGenDemo extends FlatSpec with Matchers with CoreSyntax {
  "Sawtooth state gen" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val frameFunc = Sawtooth.asFunction(OscState(0, 440.Hz, 0))
      .map(s => println(s.sample))
      .map(Array(_))

    play(frameFunc, 5 seconds)
  }
}
