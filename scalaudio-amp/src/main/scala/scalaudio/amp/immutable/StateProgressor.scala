package scalaudio.amp.immutable

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
trait StateProgressor[T] {
  def nextState(currentState: T)(implicit audioContext: AudioContext) : T
}
