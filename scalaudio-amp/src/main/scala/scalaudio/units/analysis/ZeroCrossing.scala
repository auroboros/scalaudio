package scalaudio.units.analysis

import scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 12/4/16.
  */
case class ZeroCrossingState(
                              sample: Sample,
                              crossed: Boolean,
                              previousSamplePositive: Boolean,
                              armed: Boolean
                            )

case class ZeroCrossing(threshold: Double) {

  def nextState(state: ZeroCrossingState): Boolean = ???
}