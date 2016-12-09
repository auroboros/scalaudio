package scalaudio.units.io

import java.io.File

import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.types._

/**
  * Created by johnmcgill on 12/5/16.
  */
case class Recording(filename: String)(implicit audioContext: AudioContext)
  extends SequentialState[Frame, AudioContext] {

  val waveFile: File = new File(filename + ".wav")
  val writer = ScalaudioWaveFileWriter(waveFile,
    audioContext.config.samplingRate,
    audioContext.config.nOutChannels,
    16
  )

  val c = audioContext.config
  val outBufferSize = c.framesPerBuffer * c.nOutChannels
  var bufferedOutput = Array.fill(outBufferSize)(0.0)
  var currentIndex = 0

  println(s"Writing to WAV file ${waveFile.getAbsolutePath}")

  //  TODO: override def stop(): Unit = writer.close()

  def record(audioSignal: AudioSignal) = writer.write(audioSignal)

  // TODO: Solve issue of redundant buffers / dupe code here (with Playback)
  override def nextState(frame: Frame)(implicit context: AudioContext): Frame = {
    frame.foreach { sample =>
      bufferedOutput(currentIndex) = sample
      currentIndex = (currentIndex + 1) % outBufferSize
    }

    if (currentIndex == 0) record(bufferedOutput)

    frame
  }
}