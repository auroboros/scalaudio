package com.scalaudio.amp.immutable.control

import javafx.collections.transformation.SortedList

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

import scala.collection.immutable.SortedMap

/**
  * Created by johnmcgill on 6/5/16.
  */
case class TriggerState[T](releasedTriggers: List[T],
                           remainingTriggers: SortedMap[AudioDuration, List[T]])

object TriggerStateGen {
  def nextState[T](s: TriggerState[T])(implicit audioContext: AudioContext) : TriggerState[T] = {
    val (toRelease, remaining) = s.remainingTriggers.span(_._1 < audioContext.currentTime)
    s.copy(
      releasedTriggers = toRelease.flatMap(_._2).toList,
      remainingTriggers = remaining
    )
  }
}
