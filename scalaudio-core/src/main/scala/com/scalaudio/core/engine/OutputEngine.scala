package com.scalaudio.core.engine

import com.scalaudio.core.types.MultichannelAudio

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def handleBuffer(buffers : MultichannelAudio)
}