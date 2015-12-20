package com.scalaudio

import com.jsyn.devices.{AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice

/**
  * Created by johnmcgill on 12/18/15.
  */
trait ScalaudioConfig {
  val FramesPerBuffer = 128
  val Channels = 1 // ("Samples per frame")
  val ReportClipping = true
  val SamplingRate = 44100
}

object AudioContext extends ScalaudioConfig {
  val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  val audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, SamplingRate, Channels)

  audioOutput.start
}