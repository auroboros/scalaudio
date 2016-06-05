package com.scalaudio.core.math

import com.scalaudio.core.types.AudioSignal

/**
  * Created by johnmcgill on 6/4/16.
  */
sealed trait EnergyAlgorithm // TODO: Generalize to summary algorithm?
case object RMS extends EnergyAlgorithm // Root mean square

object EnergyLevel {
  def ofSignal(signal: AudioSignal, algorithm: EnergyAlgorithm) : Double =
    algorithm match {
      case RMS => Math.sqrt(signal.map(x => Math.pow(x,2)).sum / signal.length)
    }
}