package scalaudio.units.analysis

import org.apache.commons.math3.complex.Complex
import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.math.FftMath
import scalaudio.core.types.{AudioSignal, Sample}

/**
  * Created by johnmcgill on 6/14/16.
  */
object FftAnalyzer {
  val immutable = new ImmutableFftAnalyzer{}

  // immutable utils
  def decodeInitialState(implicit audioContext: AudioContext) = FftAnalyzerState(0.0,
    None,
    Array.fill(audioContext.config.fftSize)(0),
    audioContext.config.fftSize
  )
}

case class FftAnalyzerState(sampleIn: Sample,
                            fftFrame: Option[Array[Complex]],
                            analysisBuffer: AudioSignal, // TODO: This should be queue?
                            computeInterval: Int)

trait ImmutableFftAnalyzer extends SequentialState[FftAnalyzerState, AudioContext] {

  def nextState(s: FftAnalyzerState)(implicit audioContext: AudioContext): FftAnalyzerState = {
    val compute = s.analysisBuffer.length == s.computeInterval

    s.copy(fftFrame = if (compute)
      Some(FftMath.performFFT(s.analysisBuffer))
    else None,
      analysisBuffer = if (compute) Array.empty[Double]
      else s.analysisBuffer.+:(s.sampleIn)
    )
  }
}