package scalaudio.units.control

import scalaudio.core.AudioContext
import scalaudio.core.engine.AudioFunctionGraph
import scalaudio.units.AmpTestHarness
import scala.concurrent.duration._

import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 1/2/17.
  */
class MetronomeMatcherSpec extends AmpTestHarness {
  "identical list" should "match" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 4, 2), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(1, 4, 2)) shouldBe true
  }

  "non-matching list" should "fail" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 4, 2), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(3, 4, 2)) shouldBe false
  }

  "shortened list" should "fail" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 4, 2), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(1, 4)) shouldBe false
  }

  "list elongated with 1" should "pass" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 4, 2), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(1, 4, 2, 1)) shouldBe true
  }

  "list of only 1" should "still be able to pass" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(1)) shouldBe true
  }

  "list of several 1's" should "pass too" in {
    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 1, 1), "hello")
    ))

    println(metronomeMatcher.pendingPair)
    metronomeMatcher.clockMatch(List(1)) shouldBe true
  }

  "metronome & matcher set" should "match on specified points" in {
    implicit val audioContext = AudioContext()

    val metronome = MutableMetronome(1.seconds, List(4, 3)).asReflexiveFunction()

    val metronomeMatcher = MutableMetronomeMatcher(List(
      (List(1, 1, 2), "a"),
      (List(1, 3, 1), "b"),
      (List(1, 4, 2), "c"),
      (List(2), "d"),
      (List(2, 2), "e"),
      (List(3, 1, 3), "f")
    )).asReflexiveFunction()

    var collector = ""

    AudioFunctionGraph(metronome.map(_._1).map(metronomeMatcher).map(_._1).map { res =>
      res.foreach { s =>
        println(s)
        collector += s
      }
    }).play(3.seconds)

    println(collector)

    collector shouldEqual "abcdef"
  }
}