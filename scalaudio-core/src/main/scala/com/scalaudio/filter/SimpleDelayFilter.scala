package com.scalaudio.filter

import scala.collection.mutable

/**
  * Created by johnmcgill on 1/17/16.
  */
case class SimpleDelayFilter(lengthInSamples : Int) extends Filter {
  var delayBuffers : List[mutable.Queue[Double]] = Nil

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    if (delayBuffers.isEmpty) {
      delayBuffers = List.fill(inBuffers.size)(mutable.Queue.fill(lengthInSamples)(0))
    }
    inBuffers.indices foreach (channel => inBuffers(channel) foreach (sample => delayBuffers(channel).enqueue(sample)))
    inBuffers.indices.toList map (channel => (1 to inBuffers.head.length map (_ => delayBuffers(channel).dequeue)).toArray)
  }

}