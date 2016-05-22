package com.scalaudio.filter.analysis

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 2/4/16.
  */
class EnergyAnalyzer(mode : EnergyAlgorithm) {
  def analyzeBuffers(inBuffers: List[Array[Double]]): List[Double] =
    inBuffers map (buffer => mode match {
      case RMS() => Math.sqrt(buffer.map(x => Math.pow(x,2)).sum / buffer.length)
    })
}

sealed trait EnergyAlgorithm // TODO: Generalize to summary algorithm?
// Or can this be singleton object for analysis rather than involving a housing + case classes at all?
case class RMS() extends EnergyAlgorithm // Root mean square