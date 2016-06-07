package com.scalaudio.actor.mutable.ugen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Pitch

/**
  * Created by johnmcgill on 6/5/16.
  */
class SquareActor(val initPitch: Pitch,
                  val initPhase: Double = 0)
                 (override implicit val audioContext: AudioContext) extends SineActor(initPitch, initPhase){

  override def computeFrameOut() = frameOut(0) = Math.signum(Math.sin(phase))
}