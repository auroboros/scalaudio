package com.scalaudio.amp.mutable

import com.scalaudio.amp.immutable.StateProgressor
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Sample

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