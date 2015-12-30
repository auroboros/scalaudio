package com.scalaudio.timing

import com.scalaudio.AudioContext
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 12/29/15.
  */
class TimeReleaseCapsuleTest extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
//  "Time release capsule" should "theoretically work" in {
//    val capsule : TimeReleaseCapsule[Double] = TimeReleaseCapsule(List(0 -> .0, 30 -> .2, 20 -> .1).sortBy(_._1))
//
//    println(capsule)
//
//    1 to 100 foreach (x => println(capsule.changePoints.filter(_._1 <= x).last._2))
//  }

  "Time release capsule" should "work when the prior logic is implemented in the class" in {
    val capsule : TimeReleaseCapsule[Double] = TimeReleaseCapsule(List(0 -> .0, 30 -> .2, 20 -> .1))

    println(capsule)

    1 to 100 foreach {x => AudioContext.State.currentFrame = x; println(capsule.controlValue)}
  }
}
