package com.scalaudio

import com.jsyn.devices.{AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice

/**
  * Created by johnmcgill on 12/18/15.
  */
object Config {
  val FramesPerBuffer = 128
  var Channels = 1 // ("Samples per frame")
  val ReportClipping = true
  val SamplingRate = 44100
}

object AudioContext {
  import Config._

  val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  val audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, SamplingRate, Channels)

  audioOutput.start
}