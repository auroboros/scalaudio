package scalaudio.amp.immutable.ugen

import org.scalatest.{FlatSpec, Matchers}
import signalz.{StatefulProcessor, StreamingProcessor}

import scala.concurrent.duration._
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaz._
import Scalaz._
/**
  * Created by johnmcgill on 5/29/16.
  */
class SineStateGenDemo extends FlatSpec with Matchers with CoreSyntax {
  "Sine state gen" should "produce sine audio output through outer var" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state : OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = SineStateGen.nextState(state)
      Array(state.sample)
    }

    StreamCollector(frameFunc).play(5 seconds)
  }

  "Sine state gen" should "produce sine audio output through StatefulProcessor" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val frameFunc = StatefulProcessor(SineStateGen.nextState, OscState(0, 440.Hz, 0)).nextState
      .map(oscState => Array(oscState.sample))

    StreamCollector(frameFunc).play(5 seconds)
  }

  "Sine state gen" should "produce sine audio output through StreamingProcessor" in {
    // TODO: memory leak? (because of no tail recursion?)
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    def stream = StreamingProcessor(SineStateGen.nextState, OscState(0, 440.Hz, 0)).outStream
      .map(oscState => Array(oscState.sample))

    StreamCollector(stream).play(5 seconds)
  }
}
