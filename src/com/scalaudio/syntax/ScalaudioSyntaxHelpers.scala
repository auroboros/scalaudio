package com.scalaudio.syntax

/**
  * Created by johnmcgill on 12/22/15.
  */
trait ScalaudioSyntaxHelpers {
  implicit def bufferList2ChannelSetManipulator(bufferList: List[Array[Double]]) = new ChannelSetManipulator(bufferList)
}