package com.scalaudio.unitgen.wavetable

import com.scalaudio.unitgen.{UnitGenParams, UnitGen}

/**
  * Created by johnmcgill on 1/24/16.
  */
case class WavetableGen(val mode : WavetableMode, val playbackRate : Double = 1) extends UnitGen {
  // TODO: Playback rate vs osc frequency (can be given in case class signatures...). Or maybe use wavetable osc to match in compute block...
  mode match {
    case Sine(f) =>
    case Square(f) =>
    case Sawtooth(f) =>
    case Sample(swt, fs) =>
  }

  // Updates internal buffer
  override def computeBuffer(params : Option[UnitGenParams] = None) : Unit = ???
}

abstract class WavetableMode
trait WavetableOscillator extends WavetableMode {def frequency : Double}
case class Sine(override val frequency : Double) extends WavetableOscillator
case class Square(override val frequency : Double) extends WavetableOscillator
case class Sawtooth(override val frequency : Double) extends WavetableOscillator
case class Sample(val sampleWavetable : List[Array[Double]], val samplingFreq : Int) extends WavetableMode


//object WavetableMode extends Enumeration {
//  type WavetableMode = Value
//  val Sine = Value("Sine")
//  val Square = Value("Square")
//  val Sawtooth = Value("Sawtooth")
//  val Sample = Value("Sample")
//}