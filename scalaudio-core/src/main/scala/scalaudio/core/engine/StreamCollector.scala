package scalaudio.core.engine

import scalaudio.core.AudioContext
import scalaudio.core.engine.io.OutputEngine
import scalaudio.core.types.{AudioDuration, Frame}

/**
  * Created by johnmcgill on 5/27/16.
  */
class StreamCollector(frameStream: => Stream[Frame],
                      explicitOutputEngines: Option[List[OutputEngine]] = None)
                     (implicit audioContext: AudioContext) {

  val c = audioContext.config
  val outBufferSize = c.framesPerBuffer * c.nOutChannels
  var bufferedOutput = Array.fill(outBufferSize)(0.0)
  var currentIndex = 0

  val outputEngines: List[OutputEngine] = explicitOutputEngines.getOrElse(audioContext.defaultOutputEngines)

  def start()(implicit audioContext: AudioContext) = outputEngines.foreach(_.start())

  def stop()(implicit audioContext: AudioContext) = outputEngines.foreach(_.stop())

  // Stream consumer
  def consumeFor(duration: AudioDuration)(implicit audioContext: AudioContext) =
    frameStream.take(duration.toSamples.toInt).foreach(processFrame)

  // Stream consumer
  def consumeWhile(loopCondition: (Frame) => Boolean) =
    frameStream.takeWhile(loopCondition).foreach(processFrame)

  private def processFrame(frame: Frame) = {
    frame.foreach { sample =>
      bufferedOutput(currentIndex) = sample
      currentIndex = (currentIndex + 1) % outBufferSize
    }
    audioContext.advanceBySample()

    if (audioContext.currentTime.toSamples % audioContext.config.framesPerBuffer == 0) {
      outputEngines foreach (_.handleBuffers(Left(bufferedOutput)))
    }
  }

  // convenience functions to start timeline from collector
  def play(duration: AudioDuration)(implicit audioContext: AudioContext) = {
    start()
    consumeFor(duration)
    stop()
  }

  def playWhile(loopCondition: () => Boolean)(implicit audioContext: AudioContext) = {
    start()
    consumeWhile((f: Frame) => loopCondition())
    stop()
  }
}

object StreamCollector {
  def apply(frameStream: => Stream[Frame],
            explicitOutputEngines: Option[List[OutputEngine]] = None)
           (implicit audioContext: AudioContext) : StreamCollector = new StreamCollector(frameStream, explicitOutputEngines)
}