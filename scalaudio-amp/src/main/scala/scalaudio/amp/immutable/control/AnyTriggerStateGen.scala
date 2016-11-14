package scalaudio.amp.immutable.control

import scala.collection.immutable.SortedMap
import scalaudio.amp.immutable.StateProgressor
import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/5/16.
  */
case class AnyTriggerState(releasedTriggers: List[Any],
                           remainingTriggers: SortedMap[AudioDuration, List[Any]])

object AnyTriggerStateGen extends StateProgressor[AnyTriggerState] {
  def nextState(s: AnyTriggerState)(implicit audioContext: AudioContext) : AnyTriggerState = {
    val (toRelease, remaining) = s.remainingTriggers.span(_._1 < audioContext.currentTime)
    s.copy(
      releasedTriggers = toRelease.flatMap(_._2).toList,
      remainingTriggers = remaining
    )
  }
}
