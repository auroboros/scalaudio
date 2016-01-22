package com.scalaudio.timing

import com.scalaudio.{Config, AudioContext}
import com.scalaudio.syntax.ScalaudioSyntaxHelpers

/**
  * Created by johnmcgill on 12/28/15.
  *
  */
// TODO: Would probably be more efficient to implement as mutable list & chop off outdated pieces rather than using filter
// (test on 2nd element, discard first if second now applies)
case class TimeReleaseCapsule(val initTimedEvents : List[TimedEvent]) extends ScalaudioSyntaxHelpers {
  val sortedTimedEvents = initTimedEvents.sortBy(_.startTime)

  def validateTimedEventList = ??? //TODO: Should check for overlap (start and end can be shared if they contain same val?), needs a 0 (or 1?) element, etc.

  def controlValue : Double = {
    val currentFrame = AudioContext.State.currentBuffer buffers
    val startedEvents = sortedTimedEvents.filter(_.startTime <= currentFrame)
    val inProgressEvents = startedEvents.filter(_.endTime > currentFrame) // Not greater than or equals, since final frame will be endVal anyway

    if (inProgressEvents.isEmpty) startedEvents.last.endVal
    else {
      val te = inProgressEvents.head
      te.event.valueAtRelativeTime(currentFrame - te.startTime)
    }
  }

  def signalValue : Array[Double] =
    (0 to (Config.FramesPerBuffer - 1) map {(s : Int) =>
      val currentTime = (AudioContext.State.currentBuffer buffers) + (s samples)
      val startedEvents = sortedTimedEvents.filter(_.startTime <= currentTime)
      val inProgressEvents = startedEvents.filter(_.endTime > currentTime) // Not greater than or equals, since final frame will be endVal anyway

      if (inProgressEvents.isEmpty) startedEvents.last.endVal
      else {
        val te = inProgressEvents.head
        te.event.valueAtRelativeTime(currentTime - te.startTime)
      }
    }).toArray
}