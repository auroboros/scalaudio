package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

import scala.concurrent
import scala.concurrent.duration

/**
  * Created by johnmcgill on 6/7/16.
  */
object Timeline {

  def playWhile(loopCondition: () => Boolean,
                terminals: List[OutputTerminal])(implicit audioContext: AudioContext) = {

    val advanceTick = advancerByResolution(determineResolution(terminals))
    terminals.foreach(_.start())

    while (loopCondition()) {
      terminals.foreach(_.processTick(audioContext.currentTime))
      advanceTick(audioContext)
    }

    terminals.foreach(_.stop())
  }

  // TODO: Make duration optional, have a max duration def available in audio context
  def playFor(duration: AudioDuration,
              terminals: List[OutputTerminal])(implicit audioContext: AudioContext) = {

    val resolution = determineResolution(terminals)
    val advanceTick = advancerByResolution(resolution)

    val nTicks = resolution match {
      case Samplewise =>
        duration.toSamples.toInt
      case Bufferwise =>
        duration.toBuffers.toInt
    }

    terminals.foreach(_.start())

    1 to nTicks foreach { _ =>
      terminals.foreach(_.processTick(audioContext.currentTime))
      advanceTick(audioContext)
    }

    terminals.foreach(_.stop())
  }

  private def determineResolution(terminals: List[OutputTerminal]) = {
    val processingRates: Set[TimeResolution] = terminals.map(_.processingRate).toSet

    if (processingRates.size > 1) throw new Exception("conflicting processing rates")
    else processingRates.headOption.getOrElse(throw new Exception("No output terminals so processing rate can't be determined"))
  }

  private def advancerByResolution(resolution: TimeResolution): (AudioContext) => Unit = resolution match {
    case Samplewise =>
      (ac: AudioContext) => ac.advanceBySample()
    case Bufferwise =>
      (ac: AudioContext) => ac.advanceByBuffer()
  }

}

sealed trait TimeResolution

case object Samplewise extends TimeResolution

case object Bufferwise extends TimeResolution