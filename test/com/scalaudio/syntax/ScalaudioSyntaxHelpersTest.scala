package com.scalaudio.syntax

import com.scalaudio.AudioContext
import com.scalaudio.engine.Playback
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