package scalaudio.amp.immutable.control

import signalz.SequentialState

import scala.collection.immutable.SortedMap
import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 5/29/16.
  */
case class EnvelopeState(value: Double,
                         remainingEvents: SortedMap[AudioDuration, EnvelopeSegment])

object Envelope extends SequentialState[EnvelopeState, AudioContext] {
  def nextState(s: EnvelopeState)(implicit audioContext: AudioContext): EnvelopeState = {
    val firstStartTimeOption = s.remainingEvents.headOption.map(_._1)
    val currentTime = AudioDuration(audioContext.State.currentSample)

    s.copy(
      value = firstStartTimeOption.map{ firstStartTime =>
        if (firstStartTime <= currentTime)
          s.remainingEvents.head._2.valueAtRelativeTime(currentTime - firstStartTime)
        else s.value
      }.getOrElse(s.value),
      remainingEvents = s.remainingEvents.dropWhile(re => re._2.endTime(re._1) < currentTime.nextSample)
    )
  }
}
