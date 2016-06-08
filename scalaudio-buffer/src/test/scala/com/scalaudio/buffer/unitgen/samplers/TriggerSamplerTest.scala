package com.scalaudio.buffer.unitgen.samplers

import com.scalaudio.buffer.BufferSyntax
import com.scalaudio.core.engine.{Bufferwise, Playback, Timeline}
import com.scalaudio.core.engine.bufferwise.BufferOutputTerminal
import com.scalaudio.core.{AudioContext, CoreSyntax}
import com.scalaudio.core.unitgen.samplers.{FileSample, TriggerSampler}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 2/3/16.
  */
class TriggerSamplerTest extends FlatSpec with Matchers with BufferSyntax {
  "Sampler" should "play back once when manually activated" in {
    implicit val audioContest = AudioContext()

    val sampler = new TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"))

    sampler.activateSoundSample()

    BufferOutputTerminal(sampler).play(5 seconds)
  }

  "Sampler" should "play on given beats" in {
    implicit val audioContest = AudioContext()

    val sampler = new TriggerSampler(FileSample("/Users/johnmcgill/nocode/samples/Yamaha-TG100-Whistle-C5.wav"),
      List(1 beats, 2 beats, 4 beats, 7 beats, 10 beats))

    BufferOutputTerminal(sampler).play(4 measures)
  }
}
