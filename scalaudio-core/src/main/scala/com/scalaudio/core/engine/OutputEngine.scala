package com.scalaudio.core.engine

import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def start()

  def stop()

  // Left is pre-interleaved
  def handleBuffers(buffer : Either[AudioSignal, MultichannelAudio])
}