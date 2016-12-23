package scalaudio.units.filter

import signalz.SequentialState

import scala.collection.immutable.Queue
import scalaudio.core.AudioContext
import scalaudio.core.types.{AudioDuration, Sample}

/**
  * Created by johnmcgill on 7/23/16.
  */
object SimpleDelay {
  val immutable = new ImmutableSimpleDelay {}

  // Immutable utils
  def initialState(delayDuration: AudioDuration)(implicit audioContext: AudioContext) =
    DelayFilterState(0, Queue.fill[Sample](delayDuration.toSamples.toInt)(0))
}

case class DelayFilterState(sample: Sample,
                            buffer: Queue[Sample])

trait ImmutableSimpleDelay extends SequentialState[DelayFilterState, AudioContext]{

  def nextState(currentState: DelayFilterState)(implicit audioContext: AudioContext) = {
    val (sampleOut, remainingBuffer) = currentState.buffer.dequeue
    currentState.copy(sampleOut, remainingBuffer.enqueue(currentState.sample))
  }
}