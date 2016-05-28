package com.scalaudio.syntax

import com.scalaudio.filter.mix.Splitter
import com.scalaudio.unitgen.{NoiseGen, SignalChain}
import com.scalaudio.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 1/18/16.
  */
class DurationsTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Durations" should "convert ms to secs" in {
    val dur2Sec : FiniteDuration = 2 seconds

    var someLong : Long = 4
    var someInt : Int = 3

    someInt = someLong.toInt
  }

  "Durations syntax sugar" should "convert a finite duration to samples (int or long)" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 2))

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2)))

    sigChain.play(5 seconds) // THIS IS COMPUTING SAMPLES YET PLAY WANTS FRAMES...
  }

  "Audio duration" should "convert properly" in {
    implicit val audioContext = AudioContext()

    println((5 seconds).toSamples)
    println((5 seconds).toBuffers)
  }

  "Beats" should "calc based on beats per minute" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(beatsPerMinute = 120))

    assert((2 beats).toSamples / audioContext.config.samplingRate.toDouble == 1) // 2 beats is 1 second
    assert((120 beats).toSamples / audioContext.config.samplingRate.toDouble == 60) // 120 beats is 1 minute (by definition)
  }
}
