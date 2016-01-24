package com.scalaudio

import com.jsyn.devices.{AudioDeviceInputStream, AudioDeviceManager, AudioDeviceOutputStream}
import com.jsyn.devices.javasound.JavaSoundAudioDevice
import com.scalaudio.engine.{OutputEngine, Playback}

/**
  * Created by johnmcgill on 12/18/15.
  */
case class ScalaudioConfig(val AutoStartStop : Boolean = true,
                           val FramesPerBuffer : Int = 32,
                           val NOutChannels : Int = 2, // ("Samples per frame")
                           val NInChannels : Int = 1,
                           val SamplingRate : Int = 44100,
                           val FFTBufferSize : Int = 32,
                          // Debug options
                          val DebugEnabled : Boolean = false,
                          val ReportClipping : Boolean = true,
                          // TODO: Are these still applicable? Maybe for "mix" / feed syntax...?
                          val AllowMonoSignalReplication : Boolean = true,  // If mono signal is given to input that requires multichannel, it will be copied to fill all channels
                          val AllowMultichannelSignalReplication : Boolean = true, // If multichan signal is given to input that requires more, it will be copied to fill all channels (maybe not evenly?)
                          val RequireEvenMultichannelReplication : Boolean = false, // Will fail if required chan num is not multiple of input channels
                          val AllowMultichannelSignalTruncation : Boolean = true) // If multichan signal is given to input that requires less channels, it will be truncated

case class AudioContext(val config : ScalaudioConfig = ScalaudioConfig()) {

  private val audioDevice: AudioDeviceManager = new JavaSoundAudioDevice
  // TODO: Make these private & have proxy methods for read/write (interleaving/de-interleaving can be here, actually)
  var audioOutput: AudioDeviceOutputStream = audioDevice.createOutputStream(audioDevice.getDefaultOutputDeviceID, config.SamplingRate, config.NOutChannels)
  var audioInput: AudioDeviceInputStream = audioDevice.createInputStream(audioDevice.getDefaultInputDeviceID, config.SamplingRate, config.NOutChannels)

  def start = {
    // TODO : Should check if input & output are connected & only start if they are (overwrite var from LineIn & Playback)
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