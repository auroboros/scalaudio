package com.scalaudio.syntax

import com.scalaudio.timing.{ValueChange, TimedEvent}

/**
  * Created by johnmcgill on 12/22/15.
  */
trait ScalaudioSyntaxHelpers {
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]) = new ChannelSetManipulator(bufferList)

  implicit def tuple2ValueChange(eventTuple : (Int, Double)) : TimedEvent =
    TimedEvent(eventTuple._1, ValueChange(eventTuple._2))
}