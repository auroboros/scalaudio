package com.scalaudio.implicits

/**
  * Created by johnmcgill on 12/22/15.
  */
class BufferFeeder(val bufferList : List[Array[Double]]) {
  def feed[T](nextFunc : List[Array[Double]] => T) = {
    nextFunc(bufferList)
  }
}