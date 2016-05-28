package com.scalaudio.amp.immutable.ugen

import com.scalaudio.syntax.Pitch

/**
  * Created by johnmcgill on 5/27/16.
  */
case class SineState(sample: Double,
                     pitch: Pitch,
                     phi: Double)