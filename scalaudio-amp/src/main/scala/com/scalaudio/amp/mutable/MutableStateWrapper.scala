package com.scalaudio.amp.mutable

import com.scalaudio.amp.immutable.StateProgressor
import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
case class MutableStateWrapper[T](stateProgressor: StateProgressor[T],
                                  initialState: T)
                                 (implicit val audioContext: AudioContext) {
  var state = initialState

  def nextState(preTransformer: T => T = identity, // identity?
                postTransformer: T => T = identity) : T = {
    state = postTransformer(stateProgressor.nextState(preTransformer(state)))
    state
  }
}