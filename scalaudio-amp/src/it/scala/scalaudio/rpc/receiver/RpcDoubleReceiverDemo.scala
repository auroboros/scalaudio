package scalaudio.rpc.receiver

import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.types.AudioDuration
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 6/13/16.
  */
class RpcDoubleReceiverDemo extends ScalaudioCoreTestHarness {
  "Blah" should "blah" in {
    implicit val audioContext: AudioContext = AudioContext(ScalaudioConfig(
      nOutChannels = 1,
      rpcEnabled = true))

    var state : OscState = OscState(0, 440.Hz, 0)
    val doubleReceiver = RpcDoubleReceiver("receiver_demo", 1)

    val frameFunc = () => {
      state = SineStateGen.nextState(state)
      Array(state.sample * doubleReceiver.currentValue)
    }

    AmpOutput(frameFunc).play(AudioDuration(Int.MaxValue))
  }
}
