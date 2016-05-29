package com.scalaudio.core.engine

import com.scalaudio.core.types.{AudioSignal, MultichannelAudio}

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def handleAudio(buffers : MultichannelAudio)

  def handlePreInterleavedBuffer(buffer: AudioSignal)
}