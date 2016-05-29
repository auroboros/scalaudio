package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.syntax.Pitch
import com.scalaudio.core.types._

/**
  * Created by johnmcgill on 5/29/16.
  */
case class OscState(sample: Sample,
                       pitch: Pitch,
                       phi: Double)
