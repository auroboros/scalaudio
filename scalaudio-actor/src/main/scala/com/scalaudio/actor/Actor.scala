package com.scalaudio.actor

/**
  * Created by johnmcgill on 6/5/16.
  */
trait Actor {
  def !(message: Any) : Unit = receive(message)

  def receive : PartialFunction[Any, Unit]

  def recomputeCacheableVals() : Unit = {}

  def incrementInertialState() : Unit = {}
}