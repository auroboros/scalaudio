package scalaudio.units.synth

import scala.collection.immutable.{SortedMap, TreeMap}
import scalaudio.units.control.{AdsrEnvelope, EnvelopeState, Envelope, PointEnvelope}
import scalaudio.units.ugen.{OscState, Osc}
import scalaudio.core.types.{AudioDuration, Pitch, Sample}
import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class MonosynthState(sample: Sample,
                          oscState: OscState,
                          pitchEnvState: EnvelopeState,
                          adsrEnvState: EnvelopeState)


// TODO: Make realtime by having state have a copy method that accepts a note-on?

class Monosynth(val osc: Osc) extends CoreSyntax {

  def nextState(s: MonosynthState)(implicit audioContext: AudioContext): MonosynthState = {
    val newPitchState = Envelope.nextState(s.pitchEnvState)
    val newAdsrState = Envelope.nextState(s.adsrEnvState)
    val newOscState = osc.nextState(s.oscState.copy(pitch = newPitchState.value.Hz))

    s.copy(
      sample = newOscState.sample * newAdsrState.value,
      oscState = newOscState,
      pitchEnvState = newPitchState,
      adsrEnvState = newAdsrState
    )
  }
}

object Monosynth extends CoreSyntax {
  def apply(osc: Osc) = new Monosynth(osc)

  def decodeInitialState(notes: SortedMap[AudioDuration, (Pitch, AdsrEnvelope)] = TreeMap.empty[AudioDuration, (Pitch, AdsrEnvelope)])
                        (implicit audioContext: AudioContext): MonosynthState = {
    val initPitchEnvState = EnvelopeState(
      0,
      notes.map(entry => entry._1 -> PointEnvelope(entry._2._1.freqInHz))
    )
    val initAdsrEnvState = EnvelopeState(
      0,
      notes.flatMap(entry => entry._2._2.asLinearEnvelopes(entry._1))
    )

    MonosynthState(0, //Dummy
      OscState(0, 440.Hz, 0), //Dummy
      initPitchEnvState,
      initAdsrEnvState
    )
  }
}
