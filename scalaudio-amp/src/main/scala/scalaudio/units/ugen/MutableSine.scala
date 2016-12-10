package scalaudio.units.ugen

import signalz.ReflexiveMutatingState

import scalaudio.core.AudioContext
import scalaudio.core.types.{Pitch, Sample}

/**
  * Created by johnmcgill on 12/7/16.
  */
case class MutableSine(var pitch: Pitch,
                       var phi: Double)(implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableSine, Unit, Sample] {

  var maybeW: Option[Double] = Some(computeW)

  def computeW = {
    val w = 2 * Math.PI * pitch.toHz / audioContext.config.samplingRate
    maybeW = Some(w)
    w
  }

  def calculateOutSample: Sample = Math.sin(phi)

  override def process(i: Unit, s: MutableSine): (Sample, MutableSine) = {
    phi += maybeW.getOrElse(computeW)
    (calculateOutSample, this)
  }
}