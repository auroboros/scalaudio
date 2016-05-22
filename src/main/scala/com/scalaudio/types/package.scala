package com.scalaudio

/**
  * Created by johnmcgill on 5/22/16.
  */
package object types {
  type AudioSignal = Array[Double]
  type ControlSignal = Double
  type Signal = Either[ControlSignal, AudioSignal]

  type MultiChannelAudio = List[AudioSignal]
}