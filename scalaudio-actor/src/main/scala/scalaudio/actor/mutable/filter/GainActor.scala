package scalaudio.actor.mutable.filter

import scalaudio.actor.SignalActor
import scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 6/6/16.
  */
case class SetGain(gain: Double)

class GainActor(var gain: Double) extends SignalActor {
  var bufferedFrame : Frame = Array.empty[Double]

  override def receive = {
    case SetGain(g) => gain = g
  }

  def filter(inFrame : Frame) : Frame = {
    bufferedFrame = inFrame.map(_ * gain)
    bufferedFrame
  }
}
