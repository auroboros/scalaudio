package scalaudio.buffer.timing

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 12/29/15.
  */
class TimeReleaseCapsuleTest extends FlatSpec with Matchers with CoreSyntax {
  "Time release capsule" should "work when the prior logic is implemented in the class" in {
    implicit val audioContext : AudioContext = AudioContext()

//    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List(0 -> .0, 30 -> .2, 20 -> .1))

//    println(capsule)

//    1 to 100 foreach {x => audioContext.State.currentBuffer = x; println(capsule.outputControlValue)}
  }

  "Value ramp" should "return right values" in {
    implicit val audioContext : AudioContext = AudioContext()

    val ramp = ValueRamp(10 buffers, 0, 1)
    0 to 10 foreach {x => println(ramp.valueAtRelativeTime(x buffers)) } // Is this off by one since technically the ramp spans 11 frames?
  }

  "Time release capsule" should "properly execute a value ramp" in {
    implicit val audioContext : AudioContext = AudioContext()

//    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List(0 -> .0, TimedEvent(10 buffers, ValueRamp(10 buffers, 0, 1))))

//    println(capsule)

//    1 to 30 foreach {x => audioContext.State.currentBuffer = x; println(s"Frame: $x ${capsule.outputControlValue}")}
  }

  "Composite time release capsule" should "correctly execute ADSR curve" in {
    implicit val audioContext : AudioContext = AudioContext()

//    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(List[TimedEvent](0 -> .0, TimedEvent(10 buffers, ValueRamp(10 buffers, 0, 1))) ++
//      TimedCompositeEvent(25 buffers, ADSRCurve(7 buffers, .9, 4 buffers, .45, 9 buffers, 6 buffers)))

//    println(capsule)

//    1 to 60 foreach {x => audioContext.State.currentBuffer = x; println(s"Frame: $x ${capsule.outputControlValue}")}
  }

  "Sine" should "be played through ADSR" in {
    implicit val audioContext : AudioContext = AudioContext()

    val myADSR = ADSRCurve(700.buffers, .9, 100.buffers, .45, 200.buffers, 600.buffers)
//    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(TimedCompositeEvent(1.buffers, myADSR) ++
//      TimedCompositeEvent(2000.buffers, myADSR) ++ TimedCompositeEvent(5000.buffers, myADSR))

//    val sineGen = SineGen()
//    val splitter = Splitter(2)
//    val gainController = GainFilter()
//    val frameFunc = {() => gainController.processBuffersWithControl(sineGen.outputBuffers(), capsule.outputControlValue) chain splitter.processBuffers}
//
//    StreamCollector(frameFunc).play(10000 buffers)
  }
}
