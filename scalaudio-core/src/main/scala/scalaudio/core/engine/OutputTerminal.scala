package scalaudio.core.engine

import scalaudio.core.AudioContext
import scalaudio.core.engine.io.OutputEngine
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/7/16.
  */
trait OutputTerminal {

  // TODO: declare unimplemented audioContext to use in implicits (so can be used in defaults, etc)

  val processingRate : TimeResolution

  val explicitOutputEngines: Option[List[OutputEngine]]

  def outputEngines(implicit audioContext: AudioContext) : List[OutputEngine] =
    explicitOutputEngines.getOrElse(audioContext.defaultOutputEngines)

  def preStart() = ()

  def start()(implicit audioContext: AudioContext) = {
    preStart()
    outputEngines.foreach(_.start())
  }

  def stop()(implicit audioContext: AudioContext) = outputEngines.foreach(_.stop())

  def processTick(currentTime: AudioDuration)(implicit audioContext: AudioContext)

  // convenience for when there's only 1 output (most cases?)
  def play(duration: AudioDuration)(implicit audioContext: AudioContext) = Timeline.playFor(duration, List(this))

  def playWhile(loopCondition: () => Boolean)(implicit audioContext: AudioContext) = Timeline.playWhile(loopCondition, List(this))
}