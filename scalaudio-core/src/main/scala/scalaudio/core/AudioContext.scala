package scalaudio.core

import com.jsyn.devices.javasound.JavaSoundAudioDevice
import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}

import scalaudio.core.engine.ScalaudioDeviceManager
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 11/13/16.
  */
case class AudioContext(config: ScalaudioConfig = ScalaudioConfig()) {

  preStart()

  // ~~~ AUDIO IO INITIALIZATION ~~~

  private val audioDeviceManager: AudioDeviceManager = new JavaSoundAudioDevice

  val audioOutput: AudioDeviceOutputStream = audioDeviceManager.createOutputStream(ScalaudioDeviceManager.defaultOutputDeviceID, config.samplingRate, config.nOutChannels)
  val audioInput: AudioDeviceInputStream = audioDeviceManager.createInputStream(ScalaudioDeviceManager.defaultInputDeviceID, config.samplingRate, config.nOutChannels)

  // ~~~ MUTABLE STATE CORE ~~~

  object State {
    var currentSample = 0
  }

  // ~~~ LIFECYCLE EVENTS ~~~

  def preStart() = {}

  def startAudioIO() = {
    // TODO : Should check if input & output are connected & only start if they are (overwrite var from LineIn & Playback)
    audioInput.start()
    audioOutput.start()
  }

  def stopAudioIO() = {
    audioInput.stop()
    audioOutput.stop()
  }

  // TODO: Find usages & see if this duration wrap is necessary (or can samples just be assumed from context)
  def currentTime : AudioDuration = AudioDuration(State.currentSample)(this) // For AMP

  def advanceBySample() = State.currentSample += 1
}
