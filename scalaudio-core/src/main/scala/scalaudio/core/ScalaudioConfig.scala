package scalaudio.core

/**
  * Created by johnmcgill on 12/18/15.
  */
case class ScalaudioConfig(beatsPerMinute: Double = 120,
                           beatsPerMeasure: Double = 4,
                           framesPerBuffer: Int = 32,
                           nOutChannels: Int = 2, // ("Samples per frame")
                           nInChannels: Int = 1,
                           samplingRate: Int = 44100,
                           fftSize: Int = 256,
                           rpcEnabled: Boolean = false,
                           // Debug options
                           debugEnabled: Boolean = false,
                           reportClipping: Boolean = true)
