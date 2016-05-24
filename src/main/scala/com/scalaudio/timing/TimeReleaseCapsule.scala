package com.scalaudio.timing

import com.scalaudio.AudioContext
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.UnitGen

/**
  * Created by johnmcgill on 12/28/15.
  *
  */
// TODO: Would probably be more efficient to implement as mutable list & chop off outdated pieces rather than using filter
// (test on 2nd element, discard first if second now applies)
case class TimeReleaseCapsule(initTimedEvents : List[TimedEvent])(implicit audioContext: AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  val sortedTimedEvents = initTimedEvents.sortBy(_.startTime)
  internalBuffers = List(Array.fill(audioContext.config.framesPerBuffer)(0))

  def validateTimedEventList = ??? //TODO: Should check for overlap (start and end can be shared if they contain same val?), needs a 0 (or 1?) element, etc.

  def outputControlValue : Double = {
    val currentFrame = audioContext.State.currentBuffer buffers
    val startedEvents = sortedTimedEvents.filter(_.startTime <= currentFrame)
    val inProgressEvents = startedEvents.filter(_.endTime > currentFrame) // Not greater than or equals, since final frame will be endVal anyway

    if (inProgressEvents.isEmpty) startedEvents.last.endVal
    else {
      val te = inProgressEvents.head
      te.event.valueAtRelativeTime(currentFrame - te.startTime)
    }
  }

  // Updates internal buffer
  override def computeBuffer(params : Option[UnitParams] = None): Unit =
    0 until audioContext.config.framesPerBuffer foreach { (s: Int) =>
      val currentTime = (audioContext.State.currentBuffer buffers) + (s samples)
      val startedEvents = sortedTimedEvents.filter(_.startTime <= currentTime)
      val inProgressEvents = startedEvents.filter(_.endTime > currentTime) // Not greater than or equals, since final frame will be endVal anyway

      internalBuffers.head(s) = if (inProgressEvents.isEmpty) startedEvents.last.endVal else {
          val te = inProgressEvents.head
          te.event.valueAtRelativeTime(currentTime - te.startTime)
        }
    }
}