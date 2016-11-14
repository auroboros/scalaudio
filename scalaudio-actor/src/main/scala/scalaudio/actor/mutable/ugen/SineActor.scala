package scalaudio.actor.mutable.ugen

import scalaudio.core.types.{Pitch, Sample}

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 6/5/16.
  */
case class SetPitch(pitch: Pitch)
case class SetPhase(phase: Double)

class SineActor(var pitch: Pitch,
                var phase: Double = 0.0)(implicit val audioContext: AudioContext) extends UgenActor {

  var w : Option[Double] = None
  computeW()

  var sampleOut : Sample = 0.0

  override def receive = {
    case SetPhase(p) => phase = p
    case SetPitch(p) =>
      pitch = p
      w = None // invalidate (force recompute) of w. Could also do recompute here but this is lazier. Or is eager better than doing comparisons....
  }

  def computeW() = w = Some(2 * Math.PI * pitch.toHz / audioContext.config.samplingRate)

  def computeSampleOut() = sampleOut = Math.sin(phase)

  override def recomputeCacheableVals() = {
    if (w.isEmpty) computeW()
    computeSampleOut()
  }

  override def incrementInertialState() =
    phase += w.get
}