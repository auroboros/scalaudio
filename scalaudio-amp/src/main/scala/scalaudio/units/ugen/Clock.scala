package scalaudio.units.ugen

import signalz.{ReflexiveMutatingState, SequentialState}

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/30/16.
  */
case class MutableClock(beatsPerMeasure: Int, beatDuration: AudioDuration)(implicit audioContext: AudioContext)
  extends ReflexiveMutatingState[MutableClock, Unit, Unit] {

  var measure = 1
  var beat = 1
  var remainderSamples = 0L

  def process(u: Unit, mutableClock: MutableClock): (Unit, MutableClock) = {

    if (remainderSamples == beatDuration.toSamples) {
      remainderSamples = 0
      if (beat == beatsPerMeasure) {
        beat = 1
        measure += 1
      } else beat += 1
    } else remainderSamples += 1

    ((), this)
  }
}

case class ImmutableClock(beatsPerMeasure: Int, beatDuration: AudioDuration)
  extends SequentialState[MeterReading, AudioContext] {
  // TODO: Have state include config, so can be used in preTransformer?

  def nextState(state: MeterReading)(implicit audioContext: AudioContext) = {
    val beatChange = state.remainderDuration.toSamples == beatDuration.toSamples
    val beat = if (beatChange) {
      if (state.beat == beatsPerMeasure) 0 else state.beat + 1
    } else state.beat

    state.copy(
      if (beatChange && beat == 1) state.measure + 1 else state.measure,
      beat,
      AudioDuration(if (beatChange) 0 else state.remainderDuration.nSamples + 1)
    )
  }
}

case class MeterReading(measure: Int, beat: Int, remainderDuration: AudioDuration) {

  def isMeasureOnset: Boolean = isBeatOnset && (beat == 1)

  def isBeatOnset: Boolean = remainderDuration.toSamples == 0
}
