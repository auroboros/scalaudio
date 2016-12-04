package scalaudio.units.filter

import scalaudio.core.types.{Frame, Sample}

/**
  * Created by johnmcgill on 5/28/16.
  */
object SplitFilter {
  def split(nChannels: Int)(s: Sample): Frame = Array.fill(nChannels)(s)
}