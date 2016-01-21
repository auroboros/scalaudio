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
  var SamplingRate = 44100
  var FFTBufferSize = 32

  val AllowMonoSignalReplication = true  // If mono signal is given to input that requires multichannel, it will be copied to fill all channels
  val AllowMultichannelSignalReplication = true // If multichan signal is given to input that requires more, it will be copied to fill all channels (maybe not evenly?)
  val RequireEvenMultichannelReplication = false // Will fail if required chan num is not multiple of input channels
  val AllowMultichannelSignalTruncation = true // If multichan signal is given to input that requires less channels, it will be truncated
}

object AudioContext {
  import Config._

  private val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  // TODO: Make these private & have proxy methods for read/write (interleaving/de-interleaving can be here, actually)
  var audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, SamplingRate, NOutChannels)
  var audioInput: AudioDeviceInputStream = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, SamplingRate, NOutChannels)

  start

  def start = {
    audioInput.start
    audioOutput.start
  }

  def stop = {
    audioInput.stop
    audioOutput.stop
  }

  object State {
    var currentBuffer = 0
  }

  def advanceFrame = State.currentBuffer += 1
}