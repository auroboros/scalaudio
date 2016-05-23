package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.types.MultichannelAudio

/**
  * Created by johnmcgill on 5/22/16.
  */
trait OutputTerminal {
  def audioOut(implicit audioContext: AudioContext) : MultichannelAudio
}