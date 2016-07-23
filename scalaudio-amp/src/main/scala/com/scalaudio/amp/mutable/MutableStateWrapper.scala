package com.scalaudio.amp.mutable

import com.scalaudio.amp.immutable.{AudioUnitState, StateProgressor}
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 7/10/16.
  */
case class MutableStateWrapper[T <: AudioUnitState](stateProgressor: StateProgressor[T],
                                                    initialState: T)
                                                   (implicit val audioContext: AudioContext) {
  // Allow by-name param for input sample source?

  var state = initialState

  def nextState(preTransformer: T => T = identity, // identity?
                postTransformer: T => T = identity): T = {
    state = postTransformer(
      stateProgressor.nextState(
        preTransformer(state)
      )
    )
    state
  }

//  def ->[S <: AudioUnitState](nextMsw: MutableStateWrapper[AudioUnitState]): List[MutableStateWrapper[AudioUnitState]] =
//    List(this.copy(), nextMsw)
}