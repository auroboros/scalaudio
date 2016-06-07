package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/7/16.
  */
object Timeline {
  def happen(duration: AudioDuration, terminals: List[OutputTerminal])(implicit audioContext: AudioContext) =
    1 to duration.nSamples.toInt foreach { _ =>
      terminals.foreach(_.processFrame(audioContext.currentTime))
      audioContext.advanceBySample()
    }
}
