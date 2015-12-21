package com.scalaudio

import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice

/**
  * Created by johnmcgill on 12/18/15.
  */
object Config {
  val FramesPerBuffer = 32
  var NOutChannels = 2 // ("Samples per frame")
  var NInChannels = 1
  val ReportClipping = true
  val SamplingRate = 44100
}

object AudioContext {
  import Config._

  private val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  // TODO: Make these private & have proxy methods for read/write (interleaving/de-interleaving can be here, actually)
  var audioOutput: AudioDeviceOutputStream = null
  var audioInput: AudioDeviceInputStream = null

  start

  def start = {
    // TODO: Moved here & changed above declarations to vars to accomodate testing. Any way to fork JVM for each test instead & remain immutable?
    audioOutput = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, SamplingRate, NOutChannels)
    audioInput = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, SamplingRate, NOutChannels)
    audioInput.start
    audioOutput.start
  }

  def stop = {
    audioInput.stop
    audioOutput.stop
  }

  object State {
    var currentFrame = 0
  }

  def advanceFrame = State.currentFrame += 1
}