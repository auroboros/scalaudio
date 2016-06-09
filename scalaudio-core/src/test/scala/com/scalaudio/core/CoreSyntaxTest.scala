package com.scalaudio.core

import com.scalaudio.core.engine.Playback
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 1/24/16.
  */
class CoreSyntaxTest extends FlatSpec with Matchers with CoreSyntax {
  implicit val audioContext = AudioContext()
//  override implicit def defaultOutputEngines(implicit audioContext : AudioContext) = List(Playback())

  "Implicit default output engines" should "be overridable yet remain implicit" in {
  }
}