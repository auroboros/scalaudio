package scalaudio.core.engine.io

import scalaudio.core.types._

/**
  * Created by johnmcgill on 6/6/16.
  */
case class SpeedTestDummy() extends OutputEngine {

  // TODO: Clock in here using start/stop?

  override def start(): Unit = ()

  override def stop(): Unit = ()

  // Left is pre-interleaved
  override def handleBuffers(buffer: Either[AudioSignal, MultichannelAudio]): Unit = ()
}
