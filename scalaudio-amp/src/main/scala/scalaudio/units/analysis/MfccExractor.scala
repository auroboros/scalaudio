package scalaudio.units.analysis

import be.tarsos.dsp.util.fft.{HammingWindow, FFT}
import signalz.ReflexiveMutatingState

import scalaudio.core.AudioContext
import scalaudio.core.types.Sample
import scala.collection.breakOut

/**
  * Created by johnmcgill on 1/23/17.
  *
  * NOTE: Adapted from & currently requires TarsosDSP
  */
case class MfccExractor(samplesPerFrame: Int,
                        amountOfCepstrumCoef: Int,
                        amountOfMelFilters: Int,
                        lowerFilterFreq: Float,
                        upperFilterFreq: Float)
                       (implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MfccExractor, Sample, Option[Array[Double]]] {

  // validate params?
  //  this.lowerFilterFreq = Math.max(lowerFilterFreq, 25)
  //  this.upperFilterFreq = Math.min(upperFilterFreq, audioContext.config.samplingRate / 2)

  private[mfcc] var audioFloatBuffer: Array[Float] = null
  private val fft: FFT = new FFT(samplesPerFrame, new HammingWindow)

  private[mfcc] val centerFrequencies: Array[Int] = calculateFilterBanks

  override def process(in: Sample, state: MfccExractor): (Option[Array[Float]], MfccExractor) = {

    // TODO: Collect data into float buffer & calc every overlap amt of samples
    val compute = true

    (if (compute) {
      // Magnitude Spectrum
      val bin: Array[Float] = magnitudeSpectrum(audioFloatBuffer)
      // get Mel Filterbank
      val fbank: Array[Float] = melFilter(bin, centerFrequencies)
      // Non-linear transformation
      val f: Array[Float] = nonLinearTransformation(fbank)
      // Cepstral coefficients
      Some(cepCoefficients(f))
    } else None, this)
  }

  def magnitudeSpectrum(frame: Array[Float]): Array[Float] = {
    val magSpectrum: Array[Float] = new Array[Float](frame.length)
    fft.forwardTransform(frame)

    (0 until frame.length / 2) foreach { k =>
      magSpectrum(frame.length / 2 + k) = fft.modulus(frame, frame.length / 2 - 1 - k)
      magSpectrum(frame.length / 2 - 1 - k) = magSpectrum(frame.length / 2 + k)
    }

    magSpectrum
  }

  def melFilter(bin: Array[Float], centerFrequencies: Array[Int]): Array[Float] = {
    val temp: Array[Float] = new Array[Float](amountOfMelFilters + 2)

    (1 to amountOfMelFilters) foreach { k =>
      var num1 = 0
      var num2 = 0

      var den: Float = centerFrequencies(k) - centerFrequencies(k - 1) + 1

      (centerFrequencies(k - 1) to centerFrequencies(k)) foreach { i =>
        num1 += bin(i) * (i - centerFrequencies(k - 1) + 1)
      }

      num1 /= den

      den = centerFrequencies(k + 1) - centerFrequencies(k) + 1

      (centerFrequencies(k) + 1 to centerFrequencies(k + 1)) foreach { i =>
        num2 += bin(i) * (1 - ((i - centerFrequencies(k)) / den))
      }

      temp.update(k, num1 + num2)
    }

    // fbank
    (0 until amountOfMelFilters).map(i => temp(i + 1))(breakOut)
  }

  // TODO: Where does this go... can rename to specify it is for nonlinear transformation?
  val FLOOR: Float = -50

  def nonLinearTransformation(fbank: Array[Float]): Array[Float] = fbank.indices.map { i =>
    Math.min(Math.log(fbank(i)).toFloat, FLOOR)
  }(breakOut)

  def calculateFilterBanks: Array[Int] = {

    //    length is amountOfMelFilters + 2

    val mel: Array[Double] = Array(freqToMel(lowerFilterFreq), freqToMel(upperFilterFreq))

    val factor: Float = ((mel(1) - mel(0)) / (amountOfMelFilters + 1)).toFloat

    //Calculate the centerfrequencies
    val firstBinFreq = Math.round(lowerFilterFreq / audioContext.config.samplingRate * samplesPerFrame)
    val lastBinFreq = samplesPerFrame / 2

    def generateInnerBinFreqs = () => (1 to amountOfMelFilters) map { i =>
      val fc = (inverseMel(mel(0) + factor * i) / audioContext.config.samplingRate) * samplesPerFrame
      Math.round(fc)
    }

    (Array(firstBinFreq) ++ generateInnerBinFreqs()).:+(lastBinFreq)
  }

  def cepCoefficients(f: Array[Float]): Array[Float] = (0 until amountOfCepstrumCoef).map { i =>
    f.indices.map { j =>
      f(j) * Math.cos(Math.PI * i / f.length * (j + 0.5))
    }.sum.toFloat // TODO: Make this a reduce function for less memory consumption?
  }(breakOut)

  def freqToMel(freq: Float): Float = 2595 * log10(1 + freq / 700)

  def inverseMel(x: Double): Float = (700 * (Math.pow(10, x / 2595) - 1)).toFloat

  def log10(value: Float): Float = (Math.log(value) / Math.log(10)).toFloat
}
