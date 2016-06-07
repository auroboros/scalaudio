package com.scalaudio.actor.mutable.ugen

import com.scalaudio.actor.SignalActor
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 6/5/16.
  */
trait UgenActor extends SignalActor {
  def nextSample() : Sample = { // TODO: Make this "cached" (check if current sample was already computed, rename this method to currentSampleOut?)
    recomputeCacheableVals()
    incrementInertialState()
    sampleOut
  }

  def sampleOut : Sample
}