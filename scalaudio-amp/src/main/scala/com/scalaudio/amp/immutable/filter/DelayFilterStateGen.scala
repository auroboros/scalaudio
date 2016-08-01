package com.scalaudio.amp.immutable.filter

import com.scalaudio.amp.immutable.StateProgressor
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{AudioDuration, Sample}

import scala.collection.immutable.Queue

/**
  * Created by johnmcgill on 7/23/16.
  */
case class DelayFilterState(sample: Sample,
                            buffer: Queue[Sample]) {

  def overwriteSample(s: Sample) = this.copy(sample = s)
}

object DelayFilterStateGen extends StateProgressor[DelayFilterState]{

  def initialState(delayDuration: AudioDuration)(implicit audioContext: AudioContext) =
    DelayFilterState(0, Queue.fill[Sample](delayDuration.toSamples.toInt)(0))

  def nextState(currentState: DelayFilterState)(implicit audioContext: AudioContext) = {
    val (sampleOut, remainingBuffer) = currentState.buffer.dequeue
    currentState.copy(sampleOut, remainingBuffer.enqueue(currentState.sample))
  }
}
