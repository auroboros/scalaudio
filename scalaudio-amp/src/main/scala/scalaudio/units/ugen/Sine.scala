package scalaudio.units.ugen

import scalaudio.core.CoreSyntax
import scalaudio.units.sampler.Sine

/**
  * Created by johnmcgill on 12/10/16.
  */
object SineImmutablePreferred {
//  val immutablePreferred = true
//  def apply() = if (immutablePreferred) immutable else mutable
  def apply() = ImmutableSine
//  val immutable = ImmutableSine
  val mutable = MutableSine
}

object SineMutablePreferred {
  def apply = MutableSine
  val immutable = ImmutableSine
}

object Test extends CoreSyntax{
  // Immutable preferred
  val mutableFunc = SineImmutablePreferred.mutable(440.Hz, 0).asReflexiveFunction
  val immutableFunc = SineImmutablePreferred().asFunction(OscState(0, 440.Hz, 0))
//    .asFunction(OscState(0, 440.Hz, 0))

  // Mutable preferred
  val mutableFunc2 = SineMutablePreferred(440.Hz, 0).asReflexiveFunction
  val immutableFunc2 = SineMutablePreferred.immutable.asFunction(OscState(0, 440.Hz, 0))
}