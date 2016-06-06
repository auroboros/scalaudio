package com.scalaudio.actor.mutable.ugen

import com.scalaudio.actor.Actor
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 6/5/16.
  */
trait UgenActor extends Actor {
  def nextSample() : Sample = {
    recomputeCacheableVals()
    incrementInertialState()
    sampleOut
  }

  def sampleOut : Sample
}