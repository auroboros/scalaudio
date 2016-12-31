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

  def process(u: Unit, mutableClock: MutableMetronome): (List[Int], MutableMetronome) = {
    var result:List[Int] = Nil

    remainderSamples = subdivisionDurations.scanLeft(audioContext.currentTime.toSamples){(remSamples, divDur) =>
      val n = (remSamples / divDur.toSamples.toDouble).floor
      result :+= n.toInt + 1
      (remSamples - (n * divDur.toSamples)).toLong
    }.last

    // TODO: Should put result into local var no matter so current position can be read between beats?
    (if (remainderSamples == 0) result else Nil, this)
  }
}