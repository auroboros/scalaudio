package com.scalaudio

import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/20/15.
  */
class TransposeTest extends FlatSpec with Matchers {
  "List of buffers" should "transpose & reunite correctly" in {
    val bufferList = List(Array[Double](.1, .2, .3), Array[Double](.4, .5, .6), Array[Double](.7, .8, .9)).transpose
    val newArray = bufferList.tail.foldLeft(bufferList.head)((r,c) => r ++ c).toArray
    println(newArray(0))
    val x = 1
  }
}