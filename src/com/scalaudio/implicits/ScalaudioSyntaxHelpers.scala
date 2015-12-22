package com.scalaudio.implicits

/**
  * Created by johnmcgill on 12/22/15.
  */
trait ScalaudioSyntaxHelpers {
  implicit def bufferList2BufferFeeder(bufferList: List[Array[Double]]) = new BufferFeeder(bufferList)
}
