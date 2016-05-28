package com.scalaudio.amp.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 5/28/16.
  */
case class FrameFuncAmpOutput(frameFunc : () => Frame) extends AmpOutput {

  override def sampleOut(implicit audioContext: AudioContext): Frame = frameFunc()
}