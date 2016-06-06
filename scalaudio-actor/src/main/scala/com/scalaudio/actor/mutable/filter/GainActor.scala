package com.scalaudio.actor.mutable.filter

import com.scalaudio.actor.SignalActor
import com.scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 6/6/16.
  */
case class SetGain(gain: Double)

class GainActor(var gain: Double) extends SignalActor {
  var bufferedFrame : Frame = Nil

  override def receive = {
    case SetGain(g) => gain = g
  }

  def filter(inFrame : Frame) : Frame = {
    bufferedFrame = inFrame.map(_ * gain)
    bufferedFrame
  }
}
