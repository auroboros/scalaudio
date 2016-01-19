package com.scalaudio.syntax

import com.scalaudio.timing.{TimedCompositeEvent, ValueChange, TimedEvent}
import com.sun.javafx.css.converters.DurationConverter

import scala.concurrent.duration.{Duration, DurationConversions, FiniteDuration}

/**
  * This is a bag-of-junk file to magically make all syntax work. Maybe there is a better pattern to split this up by function?
  * Could include implicits with relevant classes but then can only be really assured they're available if some flagship class
  * is imported? Considering case-by-case for now.
  *
  * Created by johnmcgill on 12/22/15.
  */
trait ScalaudioSyntaxHelpers {
  // "Signal Flow" syntax
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]) = new ChannelSetManipulator(bufferList)

  // "Durations" syntax
  // If "AudioDuration" class is created ("5 samples") this could be part of type, but still would fail auto-imports
  implicit def finiteDuration2AudioDuration(duration : FiniteDuration) : AudioDuration = AudioDuration(duration)

  implicit def int2AudioDurationRichInt(n : Int) = AudioDurationRichInt(n)

  // "Timing Events" syntax
  implicit def tuple2ValueChange(eventTuple : (Int, Double)) : TimedEvent =
    TimedEvent(eventTuple._1, ValueChange(eventTuple._2))

  implicit def timedCompositeEvent2TimedEventList(tce : TimedCompositeEvent) : List[TimedEvent] =
    tce.compositeEvent.toTimedEventList(tce.startFrame)
}
