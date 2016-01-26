package com.scalaudio.unitgen.wavetable

import java.io.File

import com.scalaudio.AudioContext
import com.scalaudio.jsyn.util.{DoubleSample, AdaptedJavaSoundSampleLoader}
import com.scalaudio.syntax.UnitParams
import com.scalaudio.unitgen.UnitGen

/**
  * Created by johnmcgill on 1/24/16.
  */
case class WavetableGen(val initMode : WavetableMode, val playbackRate : Double = 1)(implicit audioContext : AudioContext) extends UnitGen {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...

  val mode : WavetableMode = (initMode match {
    case fs @ FileSample(_) => fileSample2Sample(fs)
    case wtm : WavetableMode => wtm
  })

  val wavetable : List[Array[Double]] = mode match {
      case Sine(f) => ???
      case Square(f) => ???
      case Sawtooth(f) => ???
      case Sample(swt, fs) => swt
    }

  var position = 0
  internalBuffers = List.fill(wavetable.size)(Array.fill(audioContext.config.FramesPerBuffer)(0))

  // Updates internal buffer
  override def computeBuffer(params : Option[UnitParams] = None) : Unit =
    mode match {
      case wo : WavetableOscillator =>
      case Sample(swt, fs) => {
        0 to (wavetable.size - 1) foreach {c =>
          0 to (audioContext.config.FramesPerBuffer - 1) foreach {s => internalBuffers(c)(s) = wavetable(c)(position + s)}
        }
        position = (position + audioContext.config.FramesPerBuffer) % (audioContext.config.FramesPerBuffer - 1)
      }
    }

  def fileSample2Sample(filesample : FileSample) : Sample = {
    val doubleSample = AdaptedJavaSoundSampleLoader.loadDoubleSample(new File(filesample.filename))
    Sample(doubleSample.audioBuffers, doubleSample.frameRate)
  }
}

abstract class WavetableMode
trait WavetableOscillator extends WavetableMode {def frequency : Double}
trait WavetableSampler extends WavetableMode
case class Sine(override val frequency : Double) extends WavetableOscillator
case class Square(override val frequency : Double) extends WavetableOscillator
case class Sawtooth(override val frequency : Double) extends WavetableOscillator
case class Sample(val sampleWavetable : List[Array[Double]], val samplingFreq : Double) extends WavetableMode
case class FileSample(val filename : String) extends WavetableMode


//object WavetableMode extends Enumeration {
//  type WavetableMode = Value
//  val Sine = Value("Sine")
//  val Square = Value("Square")
//  val Sawtooth = Value("Sawtooth")
//  val Sample = Value("Sample")
//}