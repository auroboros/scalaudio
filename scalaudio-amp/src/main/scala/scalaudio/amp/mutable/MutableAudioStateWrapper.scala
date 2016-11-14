package scalaudio.amp.mutable

import scalaudio.amp.immutable.StateProgressor
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 7/10/16.
  */
case class MutableAudioStateWrapper[T <: {val sample: Sample; def overwriteSample(s: Sample): T}](override val stateProgressor: StateProgressor[T],
                                       override val initialState: T,
                                       override val preTransformer: T => T = identity[T] _, // identity?
                                       override val postTransformer: T => T = identity[T] _)
                                      (implicit val maswContext: AudioContext)
  extends MutableStateWrapper[T](stateProgressor,
    initialState,
    preTransformer,
    postTransformer)(maswContext) {

  def ~>[U <: {val sample: Sample; def overwriteSample(s: Sample): U}](nextMasw: MutableAudioStateWrapper[U]) : MutableAudioStateWrapper[U] = {
    val newPreTransform = (inputState: U) => nextMasw.preTransformer(inputState).overwriteSample(this.nextState().sample)
    nextMasw.copy(preTransformer = newPreTransform)
  }

//  def ~~[S <: {val sample: Sample; def overwritePitch(s: Sample): S}](nextMasw: MutableAudioStateWrapper[S]) : MutableAudioStateWrapper[S] = {
//    val newPreTransform = (inputState: S) => nextMasw.preTransformer(inputState).overwritePitch(this.nextState().sample)
//    nextMasw.copy(preTransformer = newPreTransform)
//  }
}