package com.scalaudio.io

import com.scalaudio.AudioContext
import com.scalaudio.engine.Playback
import com.scalaudio.unitgen.LineIn
import org.scalatest.{FlatSpec, Matchers}

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
