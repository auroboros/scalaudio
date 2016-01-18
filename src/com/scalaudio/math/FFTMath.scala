package com.scalaudio.math

import com.scalaudio.math.window.HannWindow
import com.scalaudio.Config
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer, TransformType}

/**
  * Created by johnmcgill on 1/11/16.
  */
trait FFTMath {
  import FFTMath._

  def performFFT(buffer : Array[Double]) : Array[Complex] =
     // For better design pattern this check should be in "pad", but dont want to force performance hit even when window sizes match...
      ffter.transform(applyWindow(pad(buffer)), TransformType.FORWARD)

  def performIFFT(buffer : Array[Complex]) : Array[Double] =
      ffter.transform(buffer, TransformType.INVERSE) map (_.getReal)
}

object FFTMath {
  val fftBufferSize = Config.FFTBufferSize
  val window : Array[Double] = new HannWindow(fftBufferSize).window // TODO: Some nice case class thing for windows...?

  val ffter = new FastFourierTransformer(DftNormalization.STANDARD)

  def applyWindow(buffer : Array[Double]) : Array[Double] =
    buffer zip window map {case (bs : Double, ws : Double) => bs * ws}

  def pad(buffer : Array[Double]) : Array[Double] =
    if (buffer.size == fftBufferSize) buffer
    else if (buffer.size < fftBufferSize){
      buffer ++ Array.fill(fftBufferSize-buffer.size)(0.0)
    } else {
      throw new Exception("Input buffer cannot be larger than FFT buffer size")
    }
}