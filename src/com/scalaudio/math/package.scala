package com.scalaudio

/**
  * Created by johnmcgill on 1/25/16.
  */
package object math {
  def linearInterpolate(d1 : Double, d2: Double, interpAmt : Double) : Double = {
    d1 + ((d2 - d1) * interpAmt)
  }
}
