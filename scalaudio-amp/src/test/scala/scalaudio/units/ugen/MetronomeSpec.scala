package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.AudioContext
import scala.concurrent.duration._
import scalaudio.units.AmpSyntax
import scalaz._
import Scalaz._
import scalaudio.core.engine.AudioFunctionGraph

/**
  * Created by johnmcgill on 12/31/16.
  */
class MetronomeSpec extends FlatSpec with Matchers with AmpSyntax {

  "Metronome" should "bootstrap correctly with multiple subdivisions" in {
    implicit val audioContext = AudioContext()

    val metronome = MutableMetronome(5.seconds, List(5, 2))

    metronome.subdivisionDurations shouldEqual List(5.seconds, 1.second, 500.millis).map(finiteDuration2AudioDuration)
  }

  "Metronome" should "bootstrap correctly with no subdivisions" in {
    implicit val audioContext = AudioContext()

    val metronome = MutableMetronome(5.seconds, Nil)

    metronome.subdivisionDurations shouldEqual List(5.seconds).map(finiteDuration2AudioDuration)
  }

  "Metronome" should "output result on whole structures (measures only, no subdiv)" in {
    implicit val audioContext = AudioContext()

    val metronome = MutableMetronome(1.seconds, Nil).asReflexiveFunction()

    var collector: List[List[Int]] = Nil

    AudioFunctionGraph(metronome.map { res =>
      if (res._1.nonEmpty) collector :+= res._1
    }).play(5.seconds)

    println(collector)

    collector shouldEqual List(List(1), List(2), List(3), List(4), List(5))
  }

  "Metronome" should "output result on whole structures with subdivisions" in {
    implicit val audioContext = AudioContext()

    val metronome = MutableMetronome(1.seconds, List(2, 3)).asReflexiveFunction()

    var collector: List[List[Int]] = Nil

    AudioFunctionGraph(metronome.map { res =>
      if (res._1.nonEmpty) {
        collector :+= res._1
      }
    }).play(5.seconds)

    println(collector)

    collector shouldEqual List(
      List(1, 1, 1), List(1, 1, 2), List(1, 1, 3), List(1, 2, 1), List(1, 2, 2), List(1, 2, 3),
      List(2, 1, 1), List(2, 1, 2), List(2, 1, 3), List(2, 2, 1), List(2, 2, 2), List(2, 2, 3),
      List(3, 1, 1), List(3, 1, 2), List(3, 1, 3), List(3, 2, 1), List(3, 2, 2), List(3, 2, 3),
      List(4, 1, 1), List(4, 1, 2), List(4, 1, 3), List(4, 2, 1), List(4, 2, 2), List(4, 2, 3),
      List(5, 1, 1), List(5, 1, 2), List(5, 1, 3), List(5, 2, 1), List(5, 2, 2), List(5, 2, 3)
    )
  }
}
