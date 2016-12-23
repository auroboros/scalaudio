package scalaudio.units.ugen

import signalz.ReflexiveMutatingState

import scalaudio.core.AudioContext
import scalaudio.core.types._

/**
  * Created by johnmcgill on 12/10/16.
  */

object Sine {
  def apply = MutableSine
  val immutable = new ImmutableSine{}
}

trait ImmutableSine extends ImmutableOsc {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi),
      phi = current.phi + w
    )
  }
}

// Note: If we want to retain "object purity", can always create an object a la:
//object ImmutableSineDefinitions extends ImmutableSine

case class MutableSine(initPitch: Pitch,
                       initPhi: Double)(implicit val audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableSine, Unit, Sample] {

  private var pitch = initPitch
  private var phi = initPhi

  private var maybeW: Option[Double] = Some(computeW)

  private def computeW = {
    val w = 2 * Math.PI * pitch.toHz / audioContext.config.samplingRate
    maybeW = Some(w)
    w
  }

  private def calculateOutSample: Sample = Math.sin(phi)

  // Public methods (mutators)
  def setPitch(newPitch: Pitch) = {
    maybeW = None
    pitch = newPitch
  }

  def setPhi(newPhi: Double) = {
    maybeW = None
    phi = newPhi
  }

  // Definition
  override def process(i: Unit, s: MutableSine): (Sample, MutableSine) = {
    phi += maybeW.getOrElse(computeW)
    (calculateOutSample, this)
  }
}