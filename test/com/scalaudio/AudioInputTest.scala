package com.scalaudio

import com.scalaudio.engine.Playback
import com.scalaudio.unitgen.LineIn
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers {
  "LineIn" should "be able to passthrough audio" in {
    val passthrough = new LineIn with Playback

    passthrough.play(100000)

    AudioContext.stop
  }
}
