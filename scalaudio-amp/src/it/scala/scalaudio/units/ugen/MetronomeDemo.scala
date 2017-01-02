package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.{AudioContext, ScalaudioConfig}
import scalaudio.core.engine.AudioFunctionGraph
import scalaudio.units.AmpSyntax
import scala.concurrent.duration._
import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 12/31/16.
  */
class MetronomeDemo extends FlatSpec with Matchers with AmpSyntax {
  "Metronome" should "output result on whole structures with subdivisions" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

//    val bpm = 120
//
//    val bpmDuration = 60.seconds / bpm
//
//    val n = 4

//    bpmDuration * n

    val metronome = Metronome(5.seconds, List(7)).asReflexiveFunction()
      .map(m => if (m._1.nonEmpty) println(s"m1 ${m._1}"))
      .map(Metronome(5.seconds, List(9)).asReflexiveFunction())
      .map(m => if (m._1.nonEmpty) println(s"m2 ${m._1}"))
      .map(Sine(440.Hz, 0).asReflexiveFunction()).map(_._1).map(Array(_))

    playback(metronome, 20.seconds)

    //    (2, 1)
    //    (1, 1, 1, 5)
  }

}
