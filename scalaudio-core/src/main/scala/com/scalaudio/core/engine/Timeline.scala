package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/7/16.
  */
object Timeline {

  // TODO: Make duration optional, have a max duration def available in audio context
  def happen(duration: AudioDuration,
             terminals: List[OutputTerminal])(implicit audioContext: AudioContext) = {

    val processingRates: Set[TimeResolution] = terminals.map(_.processingRate).toSet

    val resolution = if (processingRates.size > 1) throw new Exception("conflicting processing rates")
    else processingRates.headOption.getOrElse(Samplewise)


    terminals.foreach(_.start())

    val (nTicks, advanceTick) = resolution match {
      case Samplewise =>
        (duration.toSamples.toInt, (ac : AudioContext) => ac.advanceBySample())
      case Bufferwise =>
        (duration.toBuffers.toInt, (ac : AudioContext) => ac.advanceBySample())
    }

    1 to nTicks foreach { _ =>
      terminals.foreach(_.processTick(audioContext.currentTime))
      advanceTick(audioContext)
    }

    terminals.foreach(_.stop())
  }
}

sealed trait TimeResolution
case object Samplewise extends TimeResolution
case object Bufferwise extends TimeResolution