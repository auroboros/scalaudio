package com.scalaudio.core.math

import org.scalatest.{Matchers, FlatSpec}

import scala.collection.SortedMap

/**
  * Created by johnmcgill on 12/28/15.
  */
class SortedMapTest extends FlatSpec with Matchers {
  "Desired SortedMap ops" should "all work as expected" in {
    var changePoints : SortedMap[Int, Double] = SortedMap(32 -> .5, 4 -> .2, 8 -> .1, 0 -> 0)

    println(changePoints)

    1 to 50 foreach { x =>
      println("Iteration : " + x)
      println("Current val: " + removeOldAndReturnCurrent(x))
      println("Timing map: " + changePoints)
    }

    def removeOldAndReturnCurrent(frame : Int) : Double = {

      while (changePoints.size > 1 && changePoints.tail.head._1 <= frame){
        changePoints -= changePoints.head._1
      }
      println("Change points IN METHOD: " + changePoints)
      changePoints.head._2
    }
  }
}
