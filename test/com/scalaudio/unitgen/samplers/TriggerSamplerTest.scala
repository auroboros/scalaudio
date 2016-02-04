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
  "Sampler" should "play back once" in {
    implicit val audioContest = AudioContext()

    val sampler = new TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav")) with AudioTimepiece

    sampler.activateSoundSample()
    sampler.play(5 seconds)
  }
}
