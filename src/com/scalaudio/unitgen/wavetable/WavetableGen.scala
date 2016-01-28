package com.scalaudio.unitgen.wavetable

import java.io.File

import com.scalaudio.AudioContext
import com.scalaudio.jsyn.util.AdaptedJavaSoundSampleLoader
import com.scalaudio.math._
import com.scalaudio.syntax.{AudioDuration, ScalaudioSyntaxHelpers, UnitParams}
import com.scalaudio.unitgen.UnitGen

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 1/24/16.
  */
case class WavetableGen(val initMode : WavetableMode, val playbackRate : Double = 1)(implicit audioContext : AudioContext) extends UnitGen with ScalaudioSyntaxHelpers {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...

  // Determines resolution, essentially (lower = more interpolation, higher = bigger buffer)
  val periodLength : AudioDuration = 10 seconds

  val sample : Sample = initMode match {
    case fs : FileSample => fileSample2Sample(fs)
    case s : Sample => s
    case _ : Sine => Sample(generateSingleSinePeriod(periodLength), periodLength.toSamples)
    case _ : Square => Sample(generateSingleSquarePeriod(periodLength), periodLength.toSamples)
    case _ : Sawtooth => Sample(generateSingleSawtoothPeriod(periodLength), periodLength.toSamples)
  }

  val incrementRate = playbackRate * (sample.samplingFreq / audioContext.config.SamplingRate)

  def generateSingleSinePeriod(nSamples : AudioDuration) = ???
  def generateSingleSquarePeriod(nSamples : AudioDuration) = ???
  def generateSingleSawtoothPeriod(nSamples : AudioDuration) = ???

  var position : Double = 0
  internalBuffers = List.fill(sample.wavetable.size)(Array.fill(audioContext.config.FramesPerBuffer)(0))

  // Updates internal buffer
  override def computeBuffer(params : Option[UnitParams] = None) : Unit = {
    sample.wavetable.indices foreach {c =>
      0 until audioContext.config.FramesPerBuffer foreach {s => internalBuffers(c)(s) = interpolatedSample(c,(position + s * incrementRate) % sample.wavetable.head.length)}
    }
    position = (position + audioContext.config.FramesPerBuffer * incrementRate) % sample.wavetable.head.length
  }

  def interpolatedSample(channel : Int, position : Double) : Double = {
    val (ind1 : Int, ind2 : Int) = (position.floor.toInt, position.ceil.toInt % sample.wavetable.head.length)
    val interpAmount : Double = position % 1
    linearInterpolate(sample.wavetable(channel)(ind1), sample.wavetable(channel)(ind2), interpAmount)
  }

  def fileSample2Sample(filesample : FileSample) : Sample = {
    val doubleSample = AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(filesample.filename))
    Sample(doubleSample.audioBuffers, doubleSample.frameRate)
  }
}

sealed trait WavetableMode
case class Sine() extends WavetableMode
case class Square() extends WavetableMode
case class Sawtooth() extends WavetableMode
case class Sample(val wavetable : List[Array[Double]], val samplingFreq : Double) extends WavetableMode
case class FileSample(val filename : String) extends WavetableMode