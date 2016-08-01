package com.scalaudio.amp.immutable.control

import com.scalaudio.core.types.AudioDuration
import com.scalaudio.core.{AudioContext, CoreSyntax}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
/**
  * Created by johnmcgill on 7/31/16.
  */
class AnyTriggerStateGenSpec extends FlatSpec with Matchers with CoreSyntax {
  case object DummyTrigger

  "AnyTriggerStateGen" should "produce any given triggers" in {
    implicit val audioContext = AudioContext()

    // TODO: why doesn't implicit duration conversion work here?
    var anyTriggerState = AnyTriggerState(Nil, TreeMap(
      AudioDuration(3) -> List(DummyTrigger),
      AudioDuration(5) -> List(DummyTrigger)
    ))

    while (anyTriggerState.remainingTriggers.nonEmpty) {
      audioContext.advanceBySample()

      anyTriggerState = AnyTriggerStateGen.nextState(anyTriggerState)

      if (anyTriggerState.releasedTriggers.nonEmpty)
        println(anyTriggerState.releasedTriggers)
    }
  }
}
