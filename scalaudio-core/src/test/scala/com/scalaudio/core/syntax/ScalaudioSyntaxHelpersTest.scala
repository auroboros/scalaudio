package com.scalaudio.core.syntax

import com.scalaudio.core.AudioContext
import com.scalaudio.core.engine.Playback
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/24/16.
  */
class ScalaudioSyntaxHelpersTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  implicit val audioContext = AudioContext()
  override implicit def defaultOutputEngines(implicit audioContext : AudioContext) = List(Playback())

  "Implicit default output engines" should "be overridable yet remain implicit" in {
  }
}