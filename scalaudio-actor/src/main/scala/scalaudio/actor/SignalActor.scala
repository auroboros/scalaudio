package scalaudio.actor

/**
  * Created by johnmcgill on 6/5/16.
  */
trait SignalActor {
  def !(message: Any) : Unit = receive(message)

  def receive : PartialFunction[Any, Unit]

  def recomputeCacheableVals() : Unit = {}

  def incrementInertialState() : Unit = {}
}
