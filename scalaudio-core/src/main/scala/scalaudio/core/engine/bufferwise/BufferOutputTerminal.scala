package scalaudio.core.engine.bufferwise

import com.scalaudio.core.AudioContext
import scalaudio.core.engine.{Bufferwise, OutputEngine, OutputTerminal, Samplewise}
import com.scalaudio.core.types._

import scala.concurrent
import scala.concurrent.duration

/**
  * Created by johnmcgill on 5/22/16.
  */
case class BufferOutputTerminal(bufferFunc: () => MultichannelAudio,
                                explicitOutputEngines: Option[List[OutputEngine]] = None)
                               (implicit audioContext: AudioContext) extends OutputTerminal {

  val processingRate = Bufferwise

  def audioOut(implicit audioContext: AudioContext): MultichannelAudio = bufferFunc()

  def processTick(currentTime: AudioDuration)(implicit audioContext: AudioContext) =
    outputEngines foreach (_.handleBuffers(Right(audioOut)))
}