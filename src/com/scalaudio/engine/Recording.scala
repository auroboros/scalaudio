package com.scalaudio.engine

import java.io.File

import com.jsyn.util.WaveFileWriter
import com.scalaudio.{AudioContext, Config}

/**
  * Created by johnmcgill on 1/6/16.
  */
case class Recording(filename : String) extends OutputEngine {
  val waveFile: File = new File(filename + ".wav")
  val writer = new WaveFileWriter(waveFile)
  writer.setFrameRate(Config.SamplingRate)
  writer.setSamplesPerFrame(Config.NOutChannels)
  writer.setBitsPerSample(16)

  def handleBuffer(buffers : List[Array[Double]]) = record(buffers)

  def record(buffers : List[Array[Double]]) = {
    // Default is stereo, 16 bits.
//    val recorder = new WaveRecorder(synth, waveFile)
    System.out.println("Writing to WAV file " + waveFile.getAbsolutePath)

    if (buffers.length != Config.NOutChannels)
      throw new Exception("Playback -- this device outputs incompatible number of channels. This recording system requires " + Config.NOutChannels)

    writer.write(Interleaver.interleave(buffers))
  }
}
