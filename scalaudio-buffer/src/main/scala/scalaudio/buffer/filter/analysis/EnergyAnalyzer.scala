package scalaudio.buffer.filter.analysis

import scalaudio.core.math.{EnergyAlgorithm, EnergyLevel}

/**
  * Created by johnmcgill on 2/4/16.
  */
class EnergyAnalyzer(mode : EnergyAlgorithm) {
  def analyzeBuffers(inBuffers: List[Array[Double]]): List[Double] =
    inBuffers map (EnergyLevel.ofSignal(_, mode))
}