package com.scalaudio.syntax

import com.scalaudio.timing.{TimedCompositeEvent, ValueChange, TimedEvent}

/**
  * Created by johnmcgill on 12/22/15.
  */
trait ScalaudioSyntaxHelpers {
  // "Signal Flow" syntax
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]) = new ChannelSetManipulator(bufferList)

  // "Timing Events" syntax
  implicit def tuple2ValueChange(eventTuple : (Int, Double)) : TimedEvent =
    TimedEvent(eventTuple._1, ValueChange(eventTuple._2))

  implicit def timedCompositeEvent2TimedEventList(tce : TimedCompositeEvent) : List[TimedEvent] =
    tce.compositeEvent.toTimedEventList(tce.startFrame)
}