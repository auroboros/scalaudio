package scalaudio.core.engine

import scalaudio.core.AudioContext
import scalaudio.core.types.{AudioDuration, _}

/**
  * Created by johnmcgill on 12/5/16.
  */
object Timeline {
  def play(duration: AudioDuration)(implicit audioContext: AudioContext) = {
//    start()
//    consumeFor(duration)
//    stop()
  }

  def playWhile(loopCondition: () => Boolean)(implicit audioContext: AudioContext) = {
//    start()
//    consumeWhile((f: Frame) => loopCondition())
//    stop()
  }
}
