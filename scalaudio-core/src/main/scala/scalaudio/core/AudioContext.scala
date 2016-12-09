package scalaudio.core

import scalaudio.core.engine.{VirtualBidiDevice, VirtualInputDevice, VirtualOutputDevice}
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 11/13/16.
  */
case class AudioContext(config: ScalaudioConfig = ScalaudioConfig()) {

  preStart()

  // ~~~ AUDIO IO INITIALIZATION ~~~

  val audioIO = VirtualBidiDevice(config.samplingRate, config.nOutChannels)

  // ~~~ MUTABLE STATE CORE ~~~

  object State {
    var currentSample = 0
  }

  // ~~~ LIFECYCLE EVENTS ~~~

  def preStart() = {}

  // TODO: Find usages & see if this duration wrap is necessary (or can samples just be assumed from context)
  def currentTime : AudioDuration = AudioDuration(State.currentSample)(this) // For AMP

  def advanceBySample() = State.currentSample += 1
}
