package com.scalaudio.actor.mutable.ugen

import com.scalaudio.actor.SignalActor
import com.scalaudio.core.types.{Frame, Sample}

/**
  * Created by johnmcgill on 6/5/16.
  */
trait UgenActor extends SignalActor {
  def nextFrame() : Frame = { // TODO: Make this "cached" (check if current sample was already computed, rename this method to currentSampleOut?)
    recomputeCacheableVals()
    incrementInertialState()
    frameOut
  }

  def frameOut : Frame
}