package com.scalaudio.amp.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.engine.OutputEngine
import com.scalaudio.core.syntax.AudioDuration
import com.scalaudio.core.types.{Frame, MultichannelAudio}

/**
  * Created by johnmcgill on 5/27/16.
  */
trait AmpOutput {
  // TODO: Make this a class and have it accept/house the frameFunc?
  var bufferedOutput : MultichannelAudio = Nil

  // Multichannel sample (frame?)
  def sampleOut(implicit audioContext: AudioContext): Frame

  def initialize()(implicit audioContext: AudioContext) = {
    val c = audioContext.config

    if (c.autoStartStop) {
      audioContext.start()
      println("Started audio")
    }

    bufferedOutput = List.fill(c.nOutChannels)(Array.fill(c.framesPerBuffer)(0.0))
  }

  def play(duration: AudioDuration)(implicit audioContext: AudioContext, outputEngines: List[OutputEngine]) = {
    if (audioContext.State.currentSample == 0) initialize()

    1 to duration.toSamples.toInt foreach { absoluteSample =>
      audioContext.State.currentSample = absoluteSample
      val offset = absoluteSample % audioContext.config.framesPerBuffer

      if (offset == 0)
        outputEngines foreach (_.handleBuffer(bufferedOutput))

      // This all might be stupid waste of time because filling internal buffer is
      // essentially an unnecessary interleave/de-interleave? Can pass interleaved
      // 1-D array straight to output engine
      sampleOut.zipWithIndex.foreach { case (s: Double, i: Int) => bufferedOutput(i)(offset) = s }
    }

    if (audioContext.config.autoStartStop) audioContext.stop()
    println("Stopped audio")
  }
}
