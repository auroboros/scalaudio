package scalaudio.units.filter

/**
  * Created by johnmcgill on 12/4/16.
  */
object Clip {

  def clipSample(lowerLimit : Double, upperLimit : Double)(sig: Double): Double =
    if (sig < lowerLimit) {
      lowerLimit
    } else if (sig > upperLimit){
      upperLimit
    } else sig
}

