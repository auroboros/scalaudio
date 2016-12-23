package scalaudio.units.sampler

import java.io.File

import scalaudio.core.math._
import scalaudio.core.types.{AudioDuration, MultichannelAudio}
import scalaudio.core.util.AdaptedJavaSoundSampleLoader
import scalaudio.units.ugen.ImmutableSquare$

/**
  * Created by johnmcgill on 2/1/16.
  */
object SamplerUtils {
  // TODO : remove hardcoded default duration
  def wavetableMode2Sample(wtMode : WavetableType, periodLength : AudioDuration) : SoundSample =
    wtMode match {
      case fs : FileSample => fileSample2Sample(fs)
      case s : SoundSample => s
      case SineTable => SoundSample(generateSingleSinePeriod(periodLength), periodLength.toSamples)
      case SquareTable => SoundSample(generateSingleSquarePeriod(periodLength), periodLength.toSamples)
      case SawtoothTable => SoundSample(generateSingleSawtoothPeriod(periodLength), periodLength.toSamples)
    }

  def fileSample2Sample(filesample : FileSample) : SoundSample = {
    val doubleSample = AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(filesample.filename))
    SoundSample(doubleSample.audioBuffers, doubleSample.frameRate)
  }

  def generateSingleSinePeriod(duration : AudioDuration) = {
    val w = 2 * Math.PI / duration.toSamples
    List((0 until duration.toSamples.toInt map {i => Math.sin(w * i)}).toArray)
  }

  def generateSingleSquarePeriod(duration : AudioDuration) = {
    val w = 2 * Math.PI / duration.toSamples
    List((0 until duration.toSamples.toInt map {i => Math.signum(Math.sin(w * i))}).toArray)
  }

  def generateSingleSawtoothPeriod(duration : AudioDuration) =
    List((0 until duration.toSamples.toInt map {x => (x.toDouble / duration.toSamples) * 2 - 1}).toArray)
}

sealed trait WavetableType
case object SineTable extends WavetableType
case object SquareTable extends WavetableType
case object SawtoothTable extends WavetableType
case class FileSample(filename : String) extends WavetableType
case class SoundSample(wavetable : MultichannelAudio, samplingFreq : Double) extends WavetableType {
  val fileLengthInSamples: Int = wavetable.head.length

  def interpolatedSample(channel: Int, position: Double): Double = {
    val (ind1: Int, ind2: Int) = (position.floor.toInt, position.ceil.toInt % fileLengthInSamples)
    val interpAmount: Double = position % 1
    linearInterpolate(wavetable(channel)(ind1), wavetable(channel)(ind2), interpAmount)
  }
}