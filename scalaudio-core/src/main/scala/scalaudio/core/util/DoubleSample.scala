package scalaudio.core.util

import scalaudio.core.types.MultichannelAudio

/**
  * Created by johnmcgill on 1/5/16.
  */
case class DoubleSample(audioBuffers : MultichannelAudio, frameRate : Double){
  val length : Int = audioBuffers.head.length
}