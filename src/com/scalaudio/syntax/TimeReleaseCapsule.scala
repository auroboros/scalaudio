package com.scalaudio.syntax

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 12/28/15.
  *
  */
// TODO: Would probably be more efficient to implement as mutable list & chop off outdated pieces rather than using filter
// (test on 2nd element, discard first if second now applies)
case class TimeReleaseCapsule[T](val changePoints : List[(Int, T)]) {
  val sortedChangePoints = changePoints.sortBy(_._1)

  def controlValue : T =
    sortedChangePoints.filter(_._1 <= AudioContext.State.currentFrame).last._2


//  def signalValue(frame : Int) : Array[T] = {
//    Array.fill(Config.FramesPerBuffer)(changePoints.head._2)
//  }
}