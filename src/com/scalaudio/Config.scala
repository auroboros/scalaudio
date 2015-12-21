package com.scalaudio

import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice

/**
  * Created by johnmcgill on 12/18/15.
  */
object Config {
  val FramesPerBuffer = 32
  var NChannels = 1 // ("Samples per frame")
  val ReportClipping = true
  val SamplingRate = 44100
}

object AudioContext {
  import Config._

  val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  val audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, SamplingRate, NChannels)
  val audioInput: AudioDeviceInputStream = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, SamplingRate, NChannels)

  audioInput.start
  audioOutput.start

  def stop = {
    audioInput.stop
    audioOutput.stop
  }

  object State {
    var currentFrame = 0
  }
}