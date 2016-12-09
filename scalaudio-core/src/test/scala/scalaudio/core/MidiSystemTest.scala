package scalaudio.core

import javax.sound.midi._

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/8/16.
  */
class MidiSystemTest extends FlatSpec with Matchers {
  "midi" should "b" in {
    val infos = MidiSystem.getMidiDeviceInfo
  }
}
