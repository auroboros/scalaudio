package com.scalaudio.timing

import com.scalaudio.AudioContext
import com.scalaudio.engine.Playback
import com.scalaudio.filter.GainFilter
import com.scalaudio.filter.mix.Splitter
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.unitgen.{UnitGen, NoiseGen, SignalChain, SineGen}
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

  "Sine" should "be played through ADSR" in {
    val myADSR = ADSRCurve(700, .9, 100, .45, 200, 600)
    val capsule : TimeReleaseCapsule = TimeReleaseCapsule(TimedCompositeEvent(1, myADSR) ++
      TimedCompositeEvent(2000, myADSR) ++ TimedCompositeEvent(5000, myADSR))

    val sineGen = SineGen()
    val splitter = Splitter(2)
    val gainController = GainFilter()
    val frameFunc = {() => gainController.processBuffersWithControl(sineGen.outputBuffers, capsule.controlValue) feed splitter.processBuffers}
    val sigChain =  new UnitGen with Playback { override def computeBuffer: List[Array[Double]] = frameFunc() }

    sigChain.play(10000 buffers)
  }
}
