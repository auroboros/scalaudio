package scalaudio.buffer.types

import scalaudio.buffer.filter.mix.Summer

/**
  * Created by johnmcgill on 12/22/15.
  */
class ChannelSetManipulator(val channelSet : List[Array[Double]]) {
  def chain[T](nextFunc : List[Array[Double]] => T) = {
    nextFunc(channelSet)
  }

  def mix(anotherChannelSet : List[Array[Double]]) = {
    Summer.sumMultichannelBuffers(channelSet, anotherChannelSet)
  }
}