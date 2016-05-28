package com.scalaudio.core.syntax

import com.scalaudio.core.filter.mix.Summer

/**
  * Created by johnmcgill on 12/22/15.
  */
class ChannelSetManipulator(val channelSet : List[Array[Double]]) {
  def feed[T](nextFunc : List[Array[Double]] => T) = {
    nextFunc(channelSet)
  }

  def mix(anotherChannelSet : List[Array[Double]]) = {
    Summer.sumMultichannelBuffers(channelSet, anotherChannelSet)
  }
}