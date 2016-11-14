package scalaudio.core

/**
  * Created by johnmcgill on 11/13/16.
  */
trait DefaultAudioContext {
  implicit val audioContext: AudioContext = AudioContext()
}
