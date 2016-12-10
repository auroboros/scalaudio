package scalaudio.units.ugen

import signalz.MutatingState

import scalaudio.core.CoreSyntax
import scalaudio.units.sampler.Sine

/**
  * Created by johnmcgill on 12/10/16.
  */
object SineImmutablePreferred extends ImmutableSine {
//  val immutablePreferred = true
//  def apply() = if (immutablePreferred) immutable else mutable
//  val immutable = ImmutableSine
  val mutable = MutableSine
}

object SineMutablePreferred {
  def apply = MutableSine
  val immutable = new ImmutableSine{}
}

class GenericComposite[M, I](mutableType: M, immutableObj: I) {
  def apply = mutable
  val mutable = mutableType
  val immutable = immutableObj
}

object Test extends CoreSyntax{
  val gc = new GenericComposite(MutableSine, new ImmutableSine{})
  gc.immutable.asFunction(OscState(0, 440.Hz, 0))
  gc.mutable(440.Hz, 0)

  val sineI = new ImmutableSine { val mutable = MutableSine }
  sineI.mutable(440.Hz, 0)
  val sineM = new {
    def apply = MutableSine.apply _
    val immutable = new ImmutableSine{}
  }
  sineM.apply(440.Hz, 0)

  // Immutable preferred
  val mutableFunc = SineImmutablePreferred.mutable(440.Hz, 0).asReflexiveFunction
  val immutableFunc = SineImmutablePreferred.asFunction(OscState(0, 440.Hz, 0))
//    .asFunction(OscState(0, 440.Hz, 0))

  // Mutable preferred
  val mutableFunc2 = SineMutablePreferred(440.Hz, 0).asReflexiveFunction
  val immutableFunc2 = SineMutablePreferred.immutable.asFunction(OscState(0, 440.Hz, 0))
}