package com.scalaudio.io

import com.scalaudio.AudioContext
import com.scalaudio.engine.Playback
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.LineIn
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/20/15.
  */
class AudioInputTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "LineIn" should "be able to passthrough audio" in {
    val passthrough = new LineIn with Playback

    passthrough.play(100000 buffers)

    AudioContext.stop
  }
}
