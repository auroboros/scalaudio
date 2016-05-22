package com.scalaudio.unitgen.samplers

import java.io.File

import com.scalaudio.jsyn.util.AdaptedJavaSoundSampleLoader
import com.scalaudio.syntax.AudioDuration
import com.scalaudio.types._

/**
  * Created by johnmcgill on 2/1/16.
  */
object SamplerUtils {
  // TODO : remove hardcoded default duration
  def wavetableMode2Sample(wtMode : WavetableType, periodLength : AudioDuration) : SoundSample =
    wtMode match {
      case fs : FileSample => fileSample2Sample(fs)
      case s : SoundSample => s
      case _ : Sine => SoundSample(generateSingleSinePeriod(periodLength), periodLength.toSamples)
      case _ : Square => SoundSample(generateSingleSquarePeriod(periodLength), periodLength.toSamples)
      case _ : Sawtooth => SoundSample(generateSingleSawtoothPeriod(periodLength), periodLength.toSamples)
    }

  def fileSample2Sample(filesample : FileSample) : SoundSample = {
    val doubleSample = AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(filesample.filename))
    SoundSample(doubleSample.audioBuffers, doubleSample.frameRate)
  }

  // TODO: Remove method (its just for testing access from Clojure)
  def generateSingleSinePeriodFromLong(duration : Long): Array[Double] = {
    val w = 2 * Math.PI / duration
    (0 until duration.toInt map { i => Math.sin(w * i)}).toArray
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
case class Sine() extends WavetableType
case class Square() extends WavetableType
case class Sawtooth() extends WavetableType
case class SoundSample(wavetable : MultiChannelAudio, samplingFreq : Double) extends WavetableType
case class FileSample(filename : String) extends WavetableType