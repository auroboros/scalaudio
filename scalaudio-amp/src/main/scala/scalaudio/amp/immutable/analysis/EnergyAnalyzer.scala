package scalaudio.amp.immutable.analysis

import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.math.{EnergyAlgorithm, EnergyLevel, RMS}
import scalaudio.core.types.{AudioSignal, Sample}

/**
  * Created by johnmcgill on 6/4/16.
  */
case class EnergyAnalyzerState(sampleIn: Sample,
                               energyLevel: Option[Double],
                               analysisBuffer: AudioSignal = Array.fill(32)(0),
                               computeInterval: Int = 32,
                               algorithm: EnergyAlgorithm = RMS)

object EnergyAnalyzerStateGen extends SequentialState[EnergyAnalyzerState, AudioContext]{
  def nextState(s: EnergyAnalyzerState)(implicit audioContext: AudioContext): EnergyAnalyzerState = {
    val offset: Int = (audioContext.currentTime.toSamples % s.analysisBuffer.length).toInt
    s.analysisBuffer.update(offset, s.sampleIn) // will side effect on buffer with scope outside this...?

    val compute = (audioContext.currentTime.toSamples % s.computeInterval) == 0

    s.copy(
      // only fulfills option if energy level is computed this sample
      energyLevel = if (compute) Some(EnergyLevel.ofSignal(s.analysisBuffer, s.algorithm))
      else None
    )
  }
}
