package com.scalaudio.buffer

import com.scalaudio.buffer.timing.{TimedCompositeEvent, TimedEvent, ValueChange}
import com.scalaudio.buffer.types.ChannelSetManipulator
import com.scalaudio.buffer.unitgen.UnitGen
import com.scalaudio.core.types.MultichannelAudio
import com.scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 6/5/16.
  */
trait BufferSyntax extends CoreSyntax {
  // "Signal Flow" syntax
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]) : ChannelSetManipulator = new ChannelSetManipulator(bufferList)

  // "Timing Events" syntax
  implicit def tuple2ValueChange(eventTuple : (Int, Double))(implicit audioContext: AudioContext) : TimedEvent =
    TimedEvent(eventTuple._1 buffers, ValueChange(eventTuple._2))

  implicit def timedCompositeEvent2TimedEventList(tce : TimedCompositeEvent) : List[TimedEvent] =
    tce.compositeEvent.toTimedEventList(tce.startTime)

  implicit def unitGen2MultichannelAudioFunction(uGen: UnitGen)(implicit audioContext: AudioContext) : () => MultichannelAudio =
    () => uGen.outputBuffers()
}
