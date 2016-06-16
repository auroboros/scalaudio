package com.scalaudio.core.math

import com.scalaudio.core.AudioContext
import com.scalaudio.core.math.window.HannWindow
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}

/**
  * Created by johnmcgill on 1/11/16.
  */

object FFTMath {
  import window._

  def windowForConfig(implicit audioContext : AudioContext) : Array[Double] = new HannWindow(audioContext.config.fftBufferSize).window // TODO: Some nice case class thing for windows...?

  def performFFT(buffer : Array[Double])(implicit audioContext: AudioContext) : Array[Complex] =
  // For better design pattern this check should be in "pad", but dont want to force performance hit even when window sizes match...
    ffter.transform(applyWindow(pad(buffer, audioContext.config.fftBufferSize), windowForConfig), TransformType.FORWARD)

  def performIFFT(buffer : Array[Complex]) : Array[Double] =
    ffter.transform(buffer, TransformType.INVERSE) map (_.getReal)

  val ffter = new FastFourierTransformer(DftNormalization.STANDARD)

  def powerSpectrumFromFft(fft: Array[Complex]) = fft.map(bucket => Math.log(bucket.abs)).slice(0, fft.length / 2)
}