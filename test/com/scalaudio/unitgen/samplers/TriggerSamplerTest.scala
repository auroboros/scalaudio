package com.scalaudio.unitgen.samplers

import com.scalaudio.AudioContext
import com.scalaudio.engine.AudioTimepiece
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 2/3/16.
  */
class TriggerSamplerTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Sampler" should "play back once when manually activated" in {
    implicit val audioContest = AudioContext()

    val sampler = new TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav")) with AudioTimepiece

    sampler.activateSoundSample()
    sampler.play(5 seconds)
  }

  "Sampler" should "play on given beats" in {
    implicit val audioContest = AudioContext()

    val sampler = new TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"),
      List(1 beats, 2 beats, 4 beats, 7 beats, 10 beats)) with AudioTimepiece

    sampler.play(4 measures)
  }
}
