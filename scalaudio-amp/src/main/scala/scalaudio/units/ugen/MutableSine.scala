package scalaudio.units.ugen

import scalaudio.core.AudioContext
import scalaudio.core.types.{Pitch, Sample}

/**
  * Created by johnmcgill on 12/7/16.
  */
case class MutableSine(var pitch: Pitch,
                       var phi: Double)(implicit val audioContext: AudioContext)
  extends MutatingState[MutableSine, Unit, Sample] {

  var maybeW: Option[Double] = Some(computeW)

  def computeW = {
    val w = 2 * Math.PI * pitch.toHz / audioContext.config.samplingRate
    maybeW = Some(w)
    w
  }

  def calculateOutSample: Sample = Math.sin(phi)

  override def process(i: Unit, s: MutableSine): (Sample, MutableSine) = {
    phi += maybeW.getOrElse(computeW)
    (calculateOutSample, this)
  }

  def asReflexiveFunction = mutableProcessor(process, this).next
}

trait MutatingState[S, I, O] {

  def process(i: I, s: S): (O, S)

  //private?
  object mutableProcessor {
    def apply(process: (I, S) => (O, S), initState: S) = StateMutatingProcessor(process, initState)
  }

  object asFunction {
    def apply(initState: S): (I) => (O, S) = mutableProcessor(process, initState).next
  }

}

case class StateMutatingProcessor[S, I, O](process: (I, S) => (O, S),
                                           initState: S) {
  val state: S = initState

  val next: I => (O, S) = (i: I) => {
    process(i, state)
  }
}