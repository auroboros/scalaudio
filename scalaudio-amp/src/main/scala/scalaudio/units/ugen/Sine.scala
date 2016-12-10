package scalaudio.units.ugen

import scalaudio.core.CoreSyntax

/**
  * Created by johnmcgill on 12/10/16.
  */

object Sine {
  def apply = MutableSine
  val immutable = new ImmutableSine{}
}