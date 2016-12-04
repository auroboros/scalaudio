package scalaudio.amp.immutable.util

/**
  *  OptionHolderStateGen - option holder (name is an analogy to "sample & hold") accepts options but
  *  outputs a fulfilled value at every sample. If the received option is None, it simply outputs the last
  *  provided value.
  *
  *  Created by johnmcgill on 6/4/16.
  */
case class OptionHolderState[T](valueOption: Option[T],
                                lastFulfilledValue: T)

class OptionHolder[T] {
  def nextState(s: OptionHolderState[T]) : OptionHolderState[T] =
    s.valueOption match {
      case Some(x) => s.copy(lastFulfilledValue = x)
      case None => s
    }
}

object OptionHolder {
  def apply[T] = new OptionHolder[T]
}