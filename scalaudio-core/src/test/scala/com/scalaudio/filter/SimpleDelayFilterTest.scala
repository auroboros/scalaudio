package com.scalaudio.filter

import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 1/18/16.
  */
class SimpleDelayFilterTest extends FlatSpec with Matchers {
  "Simple delay" should "delay a couple channels" in {
    val delayFilter = SimpleDelayFilter(5)

    val input = List(Array(.11, .12, .13), Array(.21, .22, .23))

    1 to 5 foreach (_ => println(delayFilter.processBuffers(input) map (_.toList)))
  }
}