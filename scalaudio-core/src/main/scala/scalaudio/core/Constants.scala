package scalaudio.core

import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 11/13/16.
  */
object Constants {
  def maxDuration(implicit audioContext: AudioContext) = AudioDuration(Int.MaxValue)
}
