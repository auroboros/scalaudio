package scalaudio.core.math

import com.scalaudio.core.math.window.HannWindow
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 1/11/16.
  */

object FftMath {
  import window._

  def performFFT(buffer : Array[Double])(implicit audioContext: AudioContext) : Array[Complex] =
  // For better design pattern this check should be in "pad", but dont want to force performance hit even when window sizes match...
    ffter.transform(pad(applyWindow(buffer, new HannWindow(buffer.length).window), audioContext.config.fftSize), TransformType.FORWARD)

  def performIFFT(buffer : Array[Complex]) : Array[Double] =
    ffter.transform(buffer, TransformType.INVERSE) map (_.getReal)

  val ffter = new FastFourierTransformer(DftNormalization.STANDARD)

  def powerSpectrumFromFft(fft: Array[Complex]) = fft.map(bucket => Math.log(bucket.abs)).slice(0, fft.length / 2)
}