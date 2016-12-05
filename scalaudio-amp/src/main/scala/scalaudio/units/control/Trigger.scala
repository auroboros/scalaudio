package scalaudio.units.control

import scala.collection.immutable.SortedMap
import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/5/16.
  */
case class TriggerState[T](releasedTriggers: List[T],
                           remainingTriggers: SortedMap[AudioDuration, List[T]])

class Trigger[T] {
  def nextState(s: TriggerState[T])(implicit audioContext: AudioContext) : TriggerState[T] = {
    val (toRelease, remaining) = s.remainingTriggers.span(_._1 < audioContext.currentTime)
    s.copy(
      releasedTriggers = toRelease.flatMap(_._2).toList,
      remainingTriggers = remaining
    )
  }
}

object Trigger {
  def apply[T] = new Trigger[T]
}