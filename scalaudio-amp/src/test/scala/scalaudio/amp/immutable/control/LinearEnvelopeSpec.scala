package scalaudio.amp.immutable.control

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, CoreSyntax}
/**
  * Created by johnmcgill on 5/29/16.
  */
class LinearEnvelopeSpec extends FlatSpec with Matchers with CoreSyntax {
  "Linear envelope with 1 sec dur" should "start on start val & end on endval" in {
    implicit val audioContext = AudioContext()

    val env = LinearEnvelope(.3, .9, 1.second)
    env.valueAtRelativeTime(0.second) should equal(.3)
    env.valueAtRelativeTime(500.milliseconds).abs should equal (.6 +- 0.0001)
    env.valueAtRelativeTime(1.second).abs should equal (.9 +- 0.0001)
  }
}