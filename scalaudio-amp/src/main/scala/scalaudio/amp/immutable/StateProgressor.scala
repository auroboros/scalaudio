package scalaudio.amp.immutable

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
// TODO: Delete? Maybe worthwhile to tie type to a modifying function but its not like
// there's necessarily a 1-to-1 relationship anyway (can write many functions that work on same state object)
trait StateProgressor[T] {
  def nextState(currentState: T)(implicit audioContext: AudioContext) : T
}
