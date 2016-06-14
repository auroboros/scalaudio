package com.scalaudio.core.math

import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 6/14/16.
  */
package object window {
  def applyWindow(buffer: Array[Double], window: Array[Double])(implicit audioContext: AudioContext): Array[Double] =
    buffer zip window map { case (bs: Double, ws: Double) => bs * ws }

  def pad(buffer: Array[Double], windowLength: Int): Array[Double] =
    if (buffer.length == windowLength) buffer
    else if (buffer.length < windowLength) {
      buffer ++ Array.fill(windowLength - buffer.length)(0.0)
    } else {
      throw new Exception("Input buffer cannot be larger than target padded length")
    }
}