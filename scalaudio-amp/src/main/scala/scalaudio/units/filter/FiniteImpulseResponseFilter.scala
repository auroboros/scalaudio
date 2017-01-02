package scalaudio.units.filter

import signalz.ReflexiveMutatingState

import scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 1/1/17.
  *
  * Crudely adapted from UC Berkeley EECS Dept. site -- http://ptolemy.eecs.berkeley.edu/eecs20/week12/implementation.html
  */
object FiniteImpulseResponseFilter {
  def apply(coefs: Array[Double]) = MutableFiniteImpulseResponseFilter(coefs)
}

case class MutableFiniteImpulseResponseFilter(coefs: Array[Double])
  extends ReflexiveMutatingState[MutableFiniteImpulseResponseFilter, Sample, Sample] {

  val length = coefs.length
  val impulseResponse = coefs

  val delayLine = Array.ofDim[Double](length)
  var count = 0

  override def process(inSample: Sample, state: MutableFiniteImpulseResponseFilter): (Sample, MutableFiniteImpulseResponseFilter) = {
    delayLine(count) = inSample
    var outSample = 0.0
    var index = count
    0 until length foreach { i =>
      outSample += impulseResponse(i) * delayLine(index)
      if (index == 0) {
        index = length - 1
      } else {
        index -= 1
      }
    }
    count = (count + 1) % length

    (outSample, this)
  }
}