package com.scalaudio.engine

import java.io.File

import com.jsyn.util.WaveFileWriter
import com.scalaudio.{AudioContext, Config}

/**
  * Created by johnmcgill on 1/6/16.
  */
trait Recording {

  def outputBuffers : List[Array[Double]]

  def record(nFrames : Int, filename : String) = {
    val waveFile: File = new File(filename + ".wav")
    // Default is stereo, 16 bits.
//    val recorder = new WaveRecorder(synth, waveFile)
    System.out.println("Writing to WAV file " + waveFile.getAbsolutePath)

    val writer = new WaveFileWriter(waveFile)
    writer.setFrameRate(Config.SamplingRate)
    writer.setSamplesPerFrame(Config.NOutChannels)
    writer.setBitsPerSample(16)

    1 to nFrames foreach {_ =>
      AudioContext.advanceFrame // TODO: Need to preobsvent double advance if recording while playing back

      val obs = outputBuffers
      if (obs.length != Config.NOutChannels)
        throw new Exception("Playback -- this device outputs incompatible number of channels. This recording system requires " + Config.NOutChannels)

      writer.write(Interleaver.interleave(obs))
    }
  }
}
