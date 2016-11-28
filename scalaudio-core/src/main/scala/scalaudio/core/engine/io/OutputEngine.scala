package scalaudio.core.engine.io

import scalaudio.core.types._

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def start()

  def stop()

  // Left is pre-interleaved
  def handleBuffers(buffer : Either[AudioSignal, MultichannelAudio])
}
