package com.scalaudio.syntax

import com.scalaudio.{AudioContext, Config}
import com.scalaudio.engine.Playback
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.unitgen.{NoiseGen, SignalChain}
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
    Config.NOutChannels = 2

    val sigChain = new SignalChain(new NoiseGen, List(Splitter(2))) with Playback

    sigChain.play(5 seconds) // THIS IS COMPUTING SAMPLES YET PLAY WANTS FRAMES...

    AudioContext.audioOutput.stop
  }

  "Audio duration" should "convert properly" in {
    println(AudioDuration(5 seconds).toSamples)
    println(AudioDuration(5 seconds).toBufferConsumptions)
  }
}
