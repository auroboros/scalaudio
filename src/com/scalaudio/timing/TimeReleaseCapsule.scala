package com.scalaudio.timing

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 12/28/15.
  *
  */
// TODO: Would probably be more efficient to implement as mutable list & chop off outdated pieces rather than using filter
// (test on 2nd element, discard first if second now applies)
case class TimeReleaseCapsule(val initTimedEvents : List[TimedEvent]) {
  val sortedTimedEvents = initTimedEvents.sortBy(_.startFrame)

  def validateTimedEventList = ??? //TODO: Should check for overlap (start and end can be shared if they contain same val?), needs a 0 (or 1?) element, etc.

  def controlValue : Double = {
    val currentFrame = AudioContext.State.currentFrame
    val startedEvents = sortedTimedEvents.filter(_.startFrame <= currentFrame)
    val inProgressEvents = startedEvents.filter(_.endFrame > currentFrame) // Not greater than or equals, since final frame will be endVal anyway

    if (inProgressEvents.isEmpty) startedEvents.last.endVal
    else {
      val te = inProgressEvents.head
      te.event.valueAtRelativeFrame(currentFrame - te.startFrame)
    }
  }

  def signalValue(frame : Int) : Array[Double] = ???
}