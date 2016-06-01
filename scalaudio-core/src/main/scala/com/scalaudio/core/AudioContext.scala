package com.scalaudio.core

import com.jsyn.devices.javasound.JavaSoundAudioDevice
import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}
import com.scalaudio.core.syntax.AudioDuration

/**
  * Created by johnmcgill on 12/18/15.
  */
case class ScalaudioConfig(beatsPerMinute: Double = 120,
                           beatsPerMeasure: Double = 4,
                           autoStartStop: Boolean = true,
                           framesPerBuffer: Int = 32,
                           nOutChannels: Int = 2, // ("Samples per frame")
                           nInChannels: Int = 1,
                           samplingRate: Int = 44100,
                           fftBufferSize: Int = 32,
                           // Debug options
                           debugEnabled: Boolean = false,
                           reportClipping: Boolean = true)

case class AudioContext(config: ScalaudioConfig = ScalaudioConfig()) {

  private val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  // TODO: Make these private & have proxy methods for read/write (interleaving/de-interleaving can be here, actually)
  var audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, config.samplingRate, config.nOutChannels)
  var audioInput: AudioDeviceInputStream = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, config.samplingRate, config.nOutChannels)

  def start() = {
    // TODO : Should check if input & output are connected & only start if they are (overwrite var from LineIn & Playback)
    audioInput.start()
    audioOutput.start()
  }

  def stop() = {
    audioInput.stop()
    audioOutput.stop()
  }

  object State {
    var currentBuffer = 0
    var currentSample = 0 // For AMP //TODO: Just change this to currentTime using AudioDuration?
  }

  def currentTime : AudioDuration = AudioDuration(State.currentSample)(this) // For AMP

  def advanceByBuffer() = State.currentBuffer += 1
}