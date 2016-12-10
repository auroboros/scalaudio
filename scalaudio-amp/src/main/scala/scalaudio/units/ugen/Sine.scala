package scalaudio.units.ugen

import scalaudio.core.CoreSyntax
import scalaudio.units.sampler.Sine

/**
  * Created by johnmcgill on 12/10/16.
  */
object SineImmutablePreferred extends ImmutableSine {
  val mutable = MutableSine
}

object SineMutablePreferred {
  def apply = MutableSine
  val immutable = new ImmutableSine{}
}

object Test extends CoreSyntax{
  // Immutable preferred
  val mutableFunc = SineImmutablePreferred.mutable(440.Hz, 0).asReflexiveFunction
  val immutableFunc = SineImmutablePreferred.asFunction(OscState(0, 440.Hz, 0))

  // Mutable preferred
  val mutableFunc2 = SineMutablePreferred(440.Hz, 0).asReflexiveFunction
  val immutableFunc2 = SineMutablePreferred.immutable.asFunction(OscState(0, 440.Hz, 0)
}