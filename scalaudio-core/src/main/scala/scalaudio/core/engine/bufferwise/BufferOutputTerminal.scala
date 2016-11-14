package scalaudio.core.engine.bufferwise

import scalaudio.core.AudioContext
import scalaudio.core.engine.{Bufferwise, OutputEngine, OutputTerminal}
import scalaudio.core.types.{AudioDuration, MultichannelAudio}

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