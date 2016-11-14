package com.scalaudio.core.math.window

/**
  * Created by johnmcgill on 1/12/16.
  */
class HannWindow(override val length : Int) extends HammingWindow(length, 0.5, 0.5) {}