package scalaudio.buffer

import scalaudio.buffer.timing.{TimedCompositeEvent, TimedEvent, ValueChange}
import scalaudio.buffer.types.ChannelSetManipulator
import scalaudio.buffer.unitgen.UnitGen
import scalaudio.core.types.{Frame, MultichannelAudio}
import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 6/5/16.
  */
trait BufferSyntax extends CoreSyntax {
  // "Signal Flow" syntax
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]): ChannelSetManipulator = new ChannelSetManipulator(bufferList)

  // "Timing Events" syntax
  implicit def tuple2ValueChange(eventTuple: (Int, Double))(implicit audioContext: AudioContext): TimedEvent =
  TimedEvent(eventTuple._1 buffers, ValueChange(eventTuple._2))

  implicit def timedCompositeEvent2TimedEventList(tce: TimedCompositeEvent): List[TimedEvent] =
    tce.compositeEvent.toTimedEventList(tce.startTime)

  implicit def unitGen2MultichannelAudioFunction(uGen: UnitGen)(implicit audioContext: AudioContext): () => MultichannelAudio =
    () => uGen.outputBuffers()

  // TODO: Flattening like this is probably not super efficient, maybe should have resolution definable in "play" mode?
  // But then will require that all sig processors involved don't need sample-resolution state caching (or use other mechanism to achieve this effect...)
  implicit def bufferProducingFunc2FrameStream(bpf: () => List[Array[Double]]): Stream[Frame] = Stream.continually(bpf()).flatten

  implicit def unitGen2FrameStream(uGen: UnitGen)(implicit audioContext: AudioContext): Stream[Frame] =
    bufferProducingFunc2FrameStream(unitGen2MultichannelAudioFunction(uGen))
}