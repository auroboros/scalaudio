package scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

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
