package com.scalaudio.core

import com.scalaudio.core.engine.{OutputEngine, Playback}
import com.scalaudio.core.types._

import scala.concurrent.duration.FiniteDuration

/**
  * This is a bag-of-junk file to magically make all syntax work. Maybe there is a better pattern to split this up by function?
  * Could include implicits with relevant classes but then can only be really assured they're available if some flagship class
  * is imported? Considering case-by-case for now.
  *
  * Created by johnmcgill on 12/22/15.
  */
trait CoreSyntax {
  // "Durations" syntax
  implicit def finiteDuration2AudioDuration(duration : FiniteDuration)(implicit audioContext: AudioContext) : AudioDuration = AudioDuration(DurationConverter.finiteDuration2Samples(duration))

  implicit def int2AudioDurationRichInt(n : Int)(implicit audioContext: AudioContext) : AudioDurationRichInt = AudioDurationRichInt(n)

  // "Pitch" syntax
  implicit def int2PitchRichInt(i : Int) : PitchRichInt = PitchRichInt(i)
  implicit def double2PitchRichDouble(d : Double) : PitchRichDouble = PitchRichDouble(d)
}
