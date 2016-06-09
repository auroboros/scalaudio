package com.scalaudio.core.engine

import java.io.File

import com.jsyn.util.WaveFileWriter
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

/**
  * Created by johnmcgill on 1/6/16.
  */
case class Recording(filename: String)(implicit audioContext: AudioContext) extends OutputEngine {
  val waveFile: File = new File(filename + ".wav")
  val writer = new WaveFileWriter(waveFile)
  writer.setFrameRate(audioContext.config.samplingRate)
  writer.setSamplesPerFrame(audioContext.config.nOutChannels)
  writer.setBitsPerSample(16)

  override def start(): Unit = println(s"Writing to WAV file ${waveFile.getAbsolutePath}")

  override def stop(): Unit = writer.close()

  override def handleBuffers(output: Either[AudioSignal, MultichannelAudio]) = record(output)

  def record(output: Either[AudioSignal, MultichannelAudio]) = output match {
    case Left(audioSignal) => writer.write(audioSignal)
    case Right(multichannelAudio) => writer.write(Interleaver.interleave(multichannelAudio))
  }
}
