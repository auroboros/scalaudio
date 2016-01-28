package com.scalaudio.filter

import scala.collection.mutable

/**
  * Created by johnmcgill on 1/17/16.
  */
case class SimpleDelayFilter(val lengthInSamples : Int) extends Filter {
  var delayBuffers : List[mutable.Queue[Double]] = Nil

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    if (delayBuffers.isEmpty) {
      delayBuffers = List.fill(inBuffers.size)(mutable.Queue.fill(lengthInSamples)(0))
    }
    0 until inBuffers.size map (channel => inBuffers(channel) foreach (sample => delayBuffers(channel).enqueue(sample)))
    (0 until inBuffers.size).toList map (channel => (1 to inBuffers(0).size map (_ => delayBuffers(channel).dequeue)).toArray)
  }

}