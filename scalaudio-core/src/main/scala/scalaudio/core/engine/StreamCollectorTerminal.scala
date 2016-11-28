package scalaudio.core.engine

import scalaudio.core.AudioContext
import scalaudio.core.engine.io.OutputEngine
import scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 5/27/16.
  */
case class StreamCollectorTerminal(frameStream: Stream[Frame],
                                   explicitOutputEngines: Option[List[OutputEngine]] = None)
                                  (implicit audioContext: AudioContext) {

  def outputEngines(implicit audioContext: AudioContext) : List[OutputEngine] =
    explicitOutputEngines.getOrElse(audioContext.defaultOutputEngines)

  def processNextBuffer(implicit audioContext: AudioContext) = {
    val buffer = frameStream.take(audioContext.config.framesPerBuffer).flatten.toArray
    outputEngines foreach (_.handleBuffers(Left(buffer)))
  }
}