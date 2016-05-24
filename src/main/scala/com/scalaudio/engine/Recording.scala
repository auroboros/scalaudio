package com.scalaudio.engine

import java.io.File

import com.jsyn.util.WaveFileWriter
import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 1/6/16.
  */
case class Recording(filename : String)(implicit audioContext: AudioContext) extends OutputEngine {
  val waveFile: File = new File(filename + ".wav")
  val writer = new WaveFileWriter(waveFile)
  writer.setFrameRate(audioContext.config.samplingRate)
  writer.setSamplesPerFrame(audioContext.config.nOutChannels)
  writer.setBitsPerSample(16)

  def handleBuffer(buffers : List[Array[Double]]) = record(buffers)

  def record(buffers : List[Array[Double]]) = {
    // Default is stereo, 16 bits.
//    val recorder = new WaveRecorder(synth, waveFile)
    System.out.println("Writing to WAV file " + waveFile.getAbsolutePath)

    if (buffers.length != audioContext.config.nOutChannels)
      throw new Exception("Playback -- this device outputs incompatible number of channels. This recording system requires " + audioContext.config.nOutChannels)

    writer.write(Interleaver.interleave(buffers))
  }
}
