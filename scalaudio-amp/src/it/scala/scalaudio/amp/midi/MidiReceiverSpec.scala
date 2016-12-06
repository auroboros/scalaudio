package scalaudio.amp.midi

/**
  * Created by johnmcgill on 6/1/16.
  */

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scalaudio.core.engine.FunctionGraphTimeline
import scalaudio.core.midi.{MidiCommand, MidiConnector, QueueingMidiReceiver}
import scalaudio.core.types.Sample
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
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
      Array(0.0 : Sample)
    }

    FunctionGraphTimeline(frameFunc).play(15 seconds)
  }

}

