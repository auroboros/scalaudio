package scalaudio.core

import com.jsyn.devices.javasound.JavaSoundAudioDevice
import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}

import scalaudio.core.engine.io.Playback
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 11/13/16.
  */
case class AudioContext(config: ScalaudioConfig = ScalaudioConfig()) {

  preStart()

  // ~~~ FIELD INITIALIZATION ~~~

  private val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  // TODO: Make these private & have proxy methods for read/write (interleaving/de-interleaving can be here, actually)
  var audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, config.samplingRate, config.nOutChannels)
  var audioInput: AudioDeviceInputStream = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, config.samplingRate, config.nOutChannels)

  val defaultOutputEngines = List(Playback()(this))

  // ~~~ MUTABLE STATE CORE ~~~

  object State {
    var currentBuffer = 0
    var currentSample = 0 // For AMP //TODO: Just change this to currentTime using AudioDuration?
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

  def currentTime : AudioDuration = AudioDuration(State.currentSample)(this) // For AMP

  def advanceByBuffer() = State.currentBuffer += 1

  def advanceBySample() = State.currentSample += 1
}
