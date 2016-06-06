package com.scalaudio.amp.midi

/**
  * Created by johnmcgill on 6/1/16.
  */

import com.scalaudio.core.engine.samplewise.FrameFuncAmpOutput
import com.scalaudio.core.midi.{MidiCommand, MidiConnector, QueueingMidiReceiver}
import com.scalaudio.core.types.Sample
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
/**
  * Created by johnmcgill on 6/1/16.
  */
class MidiReceiverSpec extends ScalaudioCoreTestHarness {
  "Midi receiver" should "blah" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val pendingSynthCmds : ListBuffer[MidiCommand] = ListBuffer[MidiCommand]()
    MidiConnector.connectKeyboard(QueueingMidiReceiver(pendingSynthCmds))

    val frameFunc = () => {
      if (pendingSynthCmds.nonEmpty) println(pendingSynthCmds)
      pendingSynthCmds.clear()
      List(0.0 : Sample)
    }

    FrameFuncAmpOutput(frameFunc).play(15 seconds)
  }

}

