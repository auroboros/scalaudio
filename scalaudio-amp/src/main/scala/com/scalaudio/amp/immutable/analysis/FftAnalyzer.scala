package com.scalaudio.amp.immutable.analysis

import com.scalaudio.core.AudioContext
import com.scalaudio.core.math.FftMath
import com.scalaudio.core.types._
import org.apache.commons.math3.complex.Complex

/**
  * Created by johnmcgill on 6/14/16.
  */
case class FftAnalyzerState(sampleIn: Sample,
                            fftFrame: Option[Array[Complex]],
                            analysisBuffer: AudioSignal, // TODO: This should be queue?
                            computeInterval: Int)

object FftAnalyzerStateGen {
  def decodeInitialState(implicit audioContext: AudioContext) =
    FftAnalyzerState(0.0,
      None,
      Array.fill(audioContext.config.fftSize)(0),
      audioContext.config.fftSize)

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