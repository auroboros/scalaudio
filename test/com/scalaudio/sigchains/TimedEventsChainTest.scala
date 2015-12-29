package com.scalaudio.sigchains

import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by johnmcgill on 12/28/15.
  */
class TimedEventsChainTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Timed events in the frameFunc" should "execute at the correct moment by comparing against playback's currentFrame" in {
    // TODO: Create new type that is a wrapper, essentially hash of framenums with new param value, have that wrapped val resolve to correct current val
    // automatically (in place) without any special timing code
  }
}
