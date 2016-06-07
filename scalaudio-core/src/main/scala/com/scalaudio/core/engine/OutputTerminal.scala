package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/7/16.
  */
trait OutputTerminal {
  def processFrame(currentTime: AudioDuration)(implicit audioContext: AudioContext)
}