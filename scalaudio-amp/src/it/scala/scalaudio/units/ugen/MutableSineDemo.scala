package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, ScalaudioConfig}
import scalaudio.units.AmpSyntax
import scalaz.Scalaz._
import scalaz._
/**
  * Created by johnmcgill on 12/7/16.
  */
class MutableSineDemo extends FlatSpec with Matchers with AmpSyntax {
  "a" should "b" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    // Goofy looking because init state is.... the object itself.
    // Maybe better to just create external mutable state object no matter what
    val mutatorFunc = MutableSine(440.Hz, 0).asReflexiveFunction()

    val frameFunc = mutatorFunc.map(_._1).map(Array(_))

    playback(frameFunc, 5.seconds)
  }

  "c" should "d" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    // Goofy looking because init state is.... the object itself.
    // Maybe better to just create external mutable state object no matter what
    var freq: Double = 440

    val mutatorFunc = MutableSine(440.Hz, 0).asReflexiveFunction.
      withModifier((u: Unit, u2: Unit, s: MutableSine) => {
        s.setPitch(freq.Hz)
        freq += .1 // TODO: Create standalone func for this & pipe this in here as variable?
      }) // TODO: Implicit to erase "Unit" from signature?

    val frameFunc = mutatorFunc.curried(()).map(_._1).map(Array(_))

    playback(frameFunc, 5.seconds)
  }
}