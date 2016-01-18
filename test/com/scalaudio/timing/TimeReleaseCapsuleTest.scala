package com.scalaudio.timing

import com.scalaudio.AudioContext
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/29/15.
  */
class TimeReleaseCapsuleTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Time release capsule" should "work when the prior logic is implemented in the class" in {
    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List(0 -> .0, 30 -> .2, 20 -> .1))

    println(capsule)

    1 to 100 foreach {x => AudioContext.State.currentFrame = x; println(capsule.controlValue)}
  }

  "Value ramp" should "return right values" in {
    val ramp = ValueRamp(10, 0, 1)
    0 to 10 foreach {x => println(ramp.valueAtRelativeFrame(x)) } // Is this off by one since technically the ramp spans 11 frames?
  }

  "Time release capsule" should "properly execute a value ramp" in {
    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List(0 -> .0, TimedEvent(10, ValueRamp(10, 0, 1))))

    println(capsule)

    1 to 30 foreach {x => AudioContext.State.currentFrame = x; println(s"Frame: $x ${capsule.controlValue}")}
  }

  "Composite time release capsule" should "correctly execute ADSR curve" in {
    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List[TimedEvent](0 -> .0, TimedEvent(10, ValueRamp(10, 0, 1))) ++
      TimedCompositeEvent(25, ADSRCurve(7, .9, 4, .45, 9, 6)))

    println(capsule)

    1 to 60 foreach {x => AudioContext.State.currentFrame = x; println(s"Frame: $x ${capsule.controlValue}")}
  }
}
