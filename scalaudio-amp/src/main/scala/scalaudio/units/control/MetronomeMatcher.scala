package scalaudio.units.control

import signalz.ReflexiveMutatingState

import scala.collection.mutable

/**
  * Created by johnmcgill on 1/1/17.
  */
object MetronomeMatcher {
  def apply[A](kvPairs: Seq[(List[Int], A)]) = MutableMetronomeMatcher(kvPairs)
}

case class MutableMetronomeMatcher[A](kvPairs: Seq[(List[Int], A)])
  extends ReflexiveMutatingState[MutableMetronomeMatcher[A], List[Int], Option[A]] {

  // Assumes sorted
  val kvQueue = mutable.Queue(kvPairs: _*)

  var pendingPair = Some(kvQueue.dequeue())

  var currentProcessor: List[Int] => Option[A] = _

  val deadProcess: List[Int] => Option[A] = (clockValue: List[Int]) => None

  val activeProcess: List[Int] => Option[A] = (clock: List[Int]) => if (clockMatch(clock)){
      val outVal = pendingPair.get._2
      if (kvQueue.isEmpty) {
        currentProcessor = deadProcess
      } else {
        pendingPair = Some(kvQueue.dequeue())
      }
      Some(outVal)
  } else None

  // This compares reversed versions but actually doesn't matter
  // Can optimize with iteration (to avoid cost of reverse/drop)
  def clockMatch(clock: List[Int]): Boolean =
    clock.reverse.dropWhile(_ == 1) == pendingPair.get._1.reverse.dropWhile(_ == 1)

  // Start with active process (gets memo-ized to dead when queue is empty)
  currentProcessor = activeProcess

  override def process(clockValue: List[Int], s: MutableMetronomeMatcher[A]): (Option[A], MutableMetronomeMatcher[A]) =
    (currentProcessor(clockValue), this)
}