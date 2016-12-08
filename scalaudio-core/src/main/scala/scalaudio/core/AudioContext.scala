package scalaudio.core

import scalaudio.core.engine.{VirtualInputDevice, VirtualOutputDevice}
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 11/13/16.
  */
case class AudioContext(config: ScalaudioConfig = ScalaudioConfig()) {

  preStart()

  // ~~~ AUDIO IO INITIALIZATION ~~~

  val audioOutput = VirtualOutputDevice(config.samplingRate, config.nOutChannels)
  val audioInput = VirtualInputDevice(config.samplingRate, config.nInChannels)

  // ~~~ MUTABLE STATE CORE ~~~

  object State {
    var currentSample = 0
  }

  // ~~~ LIFECYCLE EVENTS ~~~

  def preStart() = {}

  def startAudioIO() = {
    // TODO : Should check if input & output are connected & only start if they are (overwrite var from LineIn & Playback)
    audioInput.startInput()
    audioOutput.startOutput()
  }

  def stopAudioIO() = {
    audioInput.stopInput()
    audioOutput.stop()
  }

  // TODO: Find usages & see if this duration wrap is necessary (or can samples just be assumed from context)
  def currentTime : AudioDuration = AudioDuration(State.currentSample)(this) // For AMP

  def advanceBySample() = State.currentSample += 1
}
