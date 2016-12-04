package scalaudio.units.filter

/**
  * Created by johnmcgill on 7/10/16.
  */
case class Rescaler(originalLow: Double, originalHigh: Double,
                   newLow: Double, newHigh: Double) {

  def scale(x: Double) : Double = RangeScaler.scale(this)(x)
}

object RangeScaler {
  def scale(r: Rescaler)(x: Double) : Double = r.newLow +
    (x - r.originalLow) * (r.newHigh - r.newLow) / (r.originalHigh - r.originalLow)
}