package scalaudio.amp.mutable

import scalaudio.amp.immutable.StateProgressor
import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
class MutableStateWrapper[T](val stateProgressor: StateProgressor[T],
                             val initialState: T,
                             val preTransformer: T => T = identity[T] _, // identity?
                             val postTransformer: T => T = identity[T] _)
                            (implicit val audioContext: AudioContext) {
  var state = initialState

  def nextState(): T = {
    state = postTransformer(stateProgressor.nextState(preTransformer(state)))
    state
  }

}