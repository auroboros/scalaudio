package com.scalaudio.core.math

import com.scalaudio.core.AudioContext
import com.scalaudio.core.math.window.HannWindow
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}

/**
  * Created by johnmcgill on 1/11/16.
  */
trait FFTMath {
  import FFTMath._

  def performFFT(buffer : Array[Double])(implicit audioContext: AudioContext) : Array[Complex] =
     // For better design pattern this check should be in "pad", but dont want to force performance hit even when window sizes match...
      ffter.transform(applyWindow(pad(buffer)), TransformType.FORWARD)

  def performIFFT(buffer : Array[Complex]) : Array[Double] =
      ffter.transform(buffer, TransformType.INVERSE) map (_.getReal)
}

object FFTMath {
  def window(implicit audioContext : AudioContext) : Array[Double] = new HannWindow(audioContext.config.fftBufferSize).window // TODO: Some nice case class thing for windows...?

  val ffter = new FastFourierTransformer(DftNormalization.STANDARD)

  def applyWindow(buffer : Array[Double])(implicit audioContext: AudioContext) : Array[Double] =
    buffer zip window map {case (bs : Double, ws : Double) => bs * ws}

  def pad(buffer : Array[Double])(implicit audioContext: AudioContext) : Array[Double] =
    if (buffer.length == audioContext.config.fftBufferSize) buffer
    else if (buffer.length < audioContext.config.fftBufferSize){
      buffer ++ Array.fill(audioContext.config.fftBufferSize - buffer.length)(0.0)
    } else {
      throw new Exception("Input buffer cannot be larger than FFT buffer size")
    }
}