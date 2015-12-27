package com.scalaudio.filter

/**
  * Created by johnmcgill on 12/26/15.
  */
case class ClipFilter(val lowerLimit : Double, val upperLimit : Double) extends Filter {
  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] =
    inBuffers map (_ map (sample =>
      if (sample < lowerLimit) {
        lowerLimit
      } else if (sample > upperLimit){
        upperLimit
      } else sample))
}
