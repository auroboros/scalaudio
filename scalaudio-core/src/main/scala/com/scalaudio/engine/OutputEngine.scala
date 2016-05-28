package com.scalaudio.engine

import com.scalaudio.types.MultichannelAudio

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def handleBuffer(buffers : MultichannelAudio)
}