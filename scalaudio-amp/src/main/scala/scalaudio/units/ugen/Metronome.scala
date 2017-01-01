package scalaudio.units.ugen

import signalz.ReflexiveMutatingState

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/30/16.
  */
object Metronome {
  def apply(topLevelMeasureLength: AudioDuration, subdivisions: List[Int])
           (implicit audioContext: AudioContext) = MutableMetronome(topLevelMeasureLength, subdivisions)
}

case class MutableMetronome(topLevelMeasureLength: AudioDuration, subdivisions: List[Int])
                           (implicit audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableMetronome, Unit, List[Int]] {

  val subdivisionDurations = subdivisions.scanLeft(topLevelMeasureLength)((dur, div) => AudioDuration(dur.toSamples / div))
//  val divisionLimits = Integer.MAX_VALUE :: subdivisions

  var remainderSamples = 0L
  var clock: List[Int] = Nil

  def process(u: Unit, mutableClock: MutableMetronome): (List[Int], MutableMetronome) = {
    clock = Nil

    remainderSamples = subdivisionDurations.scanLeft(audioContext.currentTime.toSamples){(remSamples, divDur) =>
      val n = (remSamples / divDur.toSamples.toDouble).floor
      clock :+= n.toInt + 1
      (remSamples - (n * divDur.toSamples)).toLong
    }.last

    (if (remainderSamples == 0) clock else Nil, this)
  }
}