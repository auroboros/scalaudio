package com.scalaudio

import com.jsyn.devices.{AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice

/**
  * Created by johnmcgill on 12/18/15.
  */
trait ScalaudioConfig {
  val FramesPerBuffer = 128
  val SamplesPerFrame = 1
}

object AudioContext extends ScalaudioConfig {
  val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  val audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, 44100, SamplesPerFrame)

  audioOutput.start
}