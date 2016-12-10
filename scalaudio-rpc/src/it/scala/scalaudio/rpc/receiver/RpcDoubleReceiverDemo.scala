package scalaudio.rpc.receiver

import scalaudio.core.types.AudioDuration
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
import scalaudio.rpc.RpcEnhancedAudioContext
import scalaudio.units.ugen.{OscState, ImmutableSine}

/**
  * Created by johnmcgill on 6/13/16.
  */
class RpcDoubleReceiverDemo extends ScalaudioCoreTestHarness {
  "Blah" should "blah" in {
    implicit val audioContext: AudioContext = RpcEnhancedAudioContext(ScalaudioConfig(
      nOutChannels = 1,
      rpcEnabled = true))

    var state : OscState = OscState(0, 440.Hz, 0)
    val doubleReceiver = RpcDoubleReceiver("receiver_demo", 1)

    val frameFunc = () => {
      state = ImmutableSine.nextState(state)
      Array(state.sample * doubleReceiver.currentValue)
    }

    frameFunc.play(AudioDuration(Int.MaxValue))
  }
}
