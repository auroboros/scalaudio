package scalaudio.units.filter

import scalaudio.core.types.{Frame, Sample}

/**
  * Created by johnmcgill on 12/4/16.
  */
object StereoPanner {
  def pan(amount: Double)(sample: Sample): Frame =
    Array(sample * Math.sqrt(1 - amount), sample * Math.sqrt(amount))
}