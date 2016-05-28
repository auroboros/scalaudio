package com.scalaudio.core.math

import org.scalatest.{FlatSpec, Matchers}

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

  "Interleaved buffer" should "deinterleave to separate channel buffers" in {
    val buffer = Array[Double](.1, .4, .7, .2, .5, .8, .3, .6, .9)
    val temp: List[Array[Double]] = buffer.grouped(3).toArray.transpose.toList
    println(temp)
    val x = 1
  }
}