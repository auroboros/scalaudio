package scalaudio.amp.immutable.filter

import scalaudio.core.types.{Frame, Sample}

/**
  * Created by johnmcgill on 5/28/16.
  */
object SplitFilter {
  def split(sample: Sample, nChannels: Int) : Frame = Array.fill(nChannels)(sample)

  // TODO: Just replace above method...
  def curriedSplit(nChannels: Int): Sample => Frame = (s: Sample) => Array.fill(nChannels)(s)
}